package com.maiya;


import com.maiya.kafka.consumer.utils.ProducerUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

/**
 * CheckJobStatus world!
 */
public class App {

    public static void main(String[] args) throws IOException {
        /*Properties pro = new Properties();

        //配置文件在jar包外
        FileInputStream in = new FileInputStream("kafkaMonitor.properties");
        pro.load(in);

        for (Object o : pro.keySet()) {
            System.out.println(pro.get(o).toString());
        }


        //配置文件在jar包内
        InputStream input = App.class.getClassLoader().getResourceAsStream("kafkaMonitor.properties");
        pro.load(input);

        for (Object o : pro.keySet()) {
            System.out.println(pro.get(o).toString());
        }*/

        //配置文件打在jar包里
        /*Properties pro = PropertiesUtils.load("kafkaMonitor.properties", KafkaConsumer.class);
        for (Object o : pro.keySet()) {
            System.out.println(pro.get(o).toString());
        }*/
        //also can be this way:
        //input = this.getClass().getResourceAsStream("/resources/config.properties");

        StringBuffer sb = new StringBuffer("");

        FileReader reader = new FileReader(args[1]);
        BufferedReader br = new BufferedReader(reader);

        ProducerUtil producer = new ProducerUtil();

        String str;
        String topic = "";
        while ((str = br.readLine()) != null) {

            try {
                String msg = str.substring(str.lastIndexOf("该条数据入库异常 : ") + 11);
                if (str.contains("ConsumerHistoryTask")) topic = "app_ph_membercallhistory";
                else if (str.contains("ConsumerMsgTask")) topic = "app_ph_membercontacts";
                else if (str.contains("ConsumerSmsTask")) topic = "app_ph_membersms";
                else {
                    System.out.println("失败.");
                    continue;
                }

                producer.send(topic, UUID.randomUUID().toString().replaceAll("-", ""), "{\"table_name\":\"".concat(topic).concat("\",\"data\":").concat(msg).concat(",\"table_status\":\"insert\"}"));
                System.out.println("成功.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("失败.");
            }
        }

        br.close();
        reader.close();
        System.out.println("发送成功");
    }

    /*public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");

        HelloWorld obj = (HelloWorld) context.getBean("helloBean");
        obj.printHello();
    }*/

}
