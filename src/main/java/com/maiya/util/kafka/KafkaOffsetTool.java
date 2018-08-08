package com.maiya.util.kafka;


import com.maiya.log.Logger;
import com.maiya.util.zookeeper.ZookeeperTool;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.zookeeper.KeeperException;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @Describe: kafka偏移量读取工具
 */
public class KafkaOffsetTool {

    public static void main(String[] args) throws Exception {

//        Properties pro = PropertiesUtils.load("kafkaMonitor.properties", KafkaConsumer.class);
//        FileInputStream inputStream1 = new FileInputStream("kafkaMonitor.properties");
        Properties pro = new Properties();
        FileInputStream in = new FileInputStream("kafkaMonitor.properties");
        pro.load(in);
        ZookeeperTool tool = new ZookeeperTool();
        //"192.168.0.113,192.168.0.114,192.168.0.115"
        tool.connectZookeeper(args[0]);
        Logger logger = new Logger(Logger.LogType.KAFKA);

        Map<String, String> kafkaParams = new HashMap<>();
        //"192.168.0.113:9092,192.168.0.114:9092,192.168.0.115:9092,192.168.0.116:9092"
        kafkaParams.put("metadata.broker.list", args[1]);

        for (Object o : pro.keySet()) {
            String[] groups = pro.get(o).toString().split(",");
            for (String group : groups) {
                Map<TopicAndPartition, Long> map1 = getLastestOffset(kafkaParams, o.toString());

                /*System.out.println("Lastest Offset");*/
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String dateString = formatter.format(currentTime);
                for (TopicAndPartition tp : map1.keySet()) {
                    String zk = "";
                    try {
                        zk = tool.getData("/consumers/" + group + "/offsets/" + o.toString() + "/" + tp.partition());
                    } catch (KeeperException e) {

                    } catch (Exception e) {

                    }

                    //System.out.println(dateString.concat(":") + " topic partition:" + tp.topic() + "-" + tp.partition() + ":" + map1.get(tp) + " " + group + " offset:" + zk);
                    if (zk.length() > 0) {
                        /*
                        *
                        select a.adddate 时间, a.name topic, a.status "offset", a.wtime "group offset", a.count "partition number", a.dtl "recover dtl" from log_kafka_int a where a.name like 'topic%' order by a.adddate desc;
                        * */
                        logger.kafka("topic-".concat(tp.topic()), Long.parseLong(zk), map1.get(tp).toString(), "set /consumers/" + group + "/offsets/" + o.toString() + "/" + tp.partition() + " ".concat(zk), tp.partition());
                    }
                }
            }


        }

        /*System.out.println("Smallest Offset");
        Map<TopicAndPartition, Long> map2 = getSmallestOffset(kafkaParams, topic);
        for (TopicAndPartition tp : map2.keySet()) {
            System.out.println(tp.topic() + "-" + tp.partition() + ":" + map2.get(tp));
        }*/

    }

    public static Map<TopicAndPartition, Long> getOffsetBeforeTimestamp(Map<String, String> kafkaParams, String topic, long whichTime) {

        Map<TopicAndPartition, Long> mapTpls = new HashMap<>();
        String brokerList = kafkaParams.get("metadata.broker.list");
        List<String> brokers = new ArrayList<>();
        int port = 9092;
        for (String s : brokerList.split(",")) {
            brokers.add(s.split(":")[0]);
            port = Integer.parseInt(s.split(":")[1]);
        }

        TreeMap<Integer, PartitionMetadata> metadatas = findLeaderBrokerList(brokers, port, topic);

        for (Entry<Integer, PartitionMetadata> entry : metadatas.entrySet()) {
            int partition = entry.getKey();
            String leadBroker = entry.getValue().leader().host();
            String clientName = "Client_" + topic + "_" + partition;
            SimpleConsumer consumer = new SimpleConsumer(leadBroker, port, 100000, 64 * 1024, clientName);
            long readOffset = getLastestOffset(consumer, topic, partition, whichTime,
                    clientName);

            TopicAndPartition tp = new TopicAndPartition(topic, partition);

            mapTpls.put(tp, readOffset);

            consumer.close();
        }

        return mapTpls;
    }

    public static Map<TopicAndPartition, Long> getLastestOffset(Map<String, String> kafkaParams, String topic) {

        return getOffsetBeforeTimestamp(kafkaParams, topic, kafka.api.OffsetRequest.LatestTime());

    }

    public static Map<TopicAndPartition, Long> getSmallestOffset(Map<String, String> kafkaParams, String topic) {

        return getOffsetBeforeTimestamp(kafkaParams, topic, kafka.api.OffsetRequest.EarliestTime());

    }

    // private List<String> m_replicaBrokers = new ArrayList<String>();
    private static long getLastestOffset(SimpleConsumer consumer, String topic, int partition, long whichTime,
                                         String clientName) {

        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<>();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
        OffsetRequest request = new OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
        OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            System.out.println(
                    "Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
            return 0;
        }
        long[] offsets = response.offsets(topic, partition);
        // long[] offsets2 = response.offsets(topic, 3);
        return offsets[0];
    }

    /**
     * 只需要一个broker，就可以发现所有的节点，
     * 通过TopicMetadataRequest发现所有的TopicMetadata，进而发现所有的PartitionMetadata
     */
    private static TreeMap<Integer, PartitionMetadata> findLeaderBrokerList(List<String> a_seedBrokers, int a_port,
                                                                            String a_topic) {

        TreeMap<Integer, PartitionMetadata> map = new TreeMap<>();
        for (String seed : a_seedBrokers) {
            SimpleConsumer consumer = null;
            try {
                consumer = new SimpleConsumer(seed, a_port, 100000, 64 * 1024, "leaderLookup" + new Date().getTime());
                List<String> topics = Collections.singletonList(a_topic);
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

                List<TopicMetadata> metaData = resp.topicsMetadata();
                for (TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        map.put(part.partitionId(), part);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error communicating with Broker [" + seed + "] to find Leader for [" + a_topic + ", ] Reason: " + e);
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }

        return map;
    }

    public static void test() {
        // 读取kafka最新数据
        // Properties props = new Properties();
        // props.put("zookeeper.connect",
        // "192.168.6.18:2181,192.168.6.20:2181,192.168.6.44:2181,192.168.6.237:2181,192.168.6.238:2181/kafka-zk");
        // props.put("zk.connectiontimeout.ms", "1000000");
        // props.put("group.id", "dirk_group");
        //
        // ConsumerConfig consumerConfig = new ConsumerConfig(props);
        // ConsumerConnector connector =
        // Consumer.createJavaConsumerConnector(consumerConfig);

        String topic = "hy_membersms";
        String seed = "192.168.0.113";
        int port = 9092;
        List<String> seeds = new ArrayList<>();
        seeds.add(seed);

        TreeMap<Integer, PartitionMetadata> metadatas = findLeaderBrokerList(seeds, port, topic);

        int sum = 0;

        for (Entry<Integer, PartitionMetadata> entry : metadatas.entrySet()) {
            int partition = entry.getKey();
            String leadBroker = entry.getValue().leader().host();
            String clientName = "Client_" + topic + "_" + partition;
            SimpleConsumer consumer = new SimpleConsumer(leadBroker, port, 100000, 64 * 1024, clientName);
            long readOffset = getLastestOffset(consumer, topic, partition, kafka.api.OffsetRequest.LatestTime(), clientName);
            sum += readOffset;
            System.out.println(partition + ":" + readOffset);
            consumer.close();
        }
        System.out.println("总和：" + sum);
    }

}
