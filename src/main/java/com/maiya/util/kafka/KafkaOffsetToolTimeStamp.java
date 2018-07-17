package com.maiya.util.kafka;


import com.maiya.util.zookeeper.ZookeeperTool;
import kafka.common.TopicAndPartition;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Describe: kafka偏移量读取工具
 */
public class KafkaOffsetToolTimeStamp {

    public static void main(String[] args) throws Exception {

        ZookeeperTool tool = new ZookeeperTool();
        //"192.168.0.113,192.168.0.114,192.168.0.115"
        tool.connectZookeeper(args[0]);

        Map<String, String> kafkaParams = new HashMap<>();
        String topic = args[2];
        //"192.168.0.113:9092,192.168.0.114:9092,192.168.0.115:9092,192.168.0.116:9092"
        kafkaParams.put("metadata.broker.list", args[1]);
        long whichTime = Long.parseLong(args[4]);

        Map<TopicAndPartition, Long> map1 = KafkaOffsetTool.getOffsetBeforeTimestamp(kafkaParams, topic, whichTime);

        System.out.println("Offset of time ".concat(new Date(whichTime).toString()));
        for (TopicAndPartition tp : map1.keySet()) {
            String zk = "";
            try {
                zk = tool.getData("/consumers/" + args[3] + "/offsets/" + topic + "/" + tp.partition());
            } catch (Exception e) {

            }
            System.out.println(tp.topic() + "-" + tp.partition() + ":" + map1.get(tp) + " " + args[3] + " offset:" + zk);
        }

        /*System.out.println("Smallest Offset");
        Map<TopicAndPartition, Long> map2 = getSmallestOffset(kafkaParams, topic);
        for (TopicAndPartition tp : map2.keySet()) {
            System.out.println(tp.topic() + "-" + tp.partition() + ":" + map2.get(tp));
        }*/

    }


}
