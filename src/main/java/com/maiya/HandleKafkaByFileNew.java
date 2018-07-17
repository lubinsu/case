package com.maiya;

import com.maiya.kafka.consumer.utils.ProducerUtil;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by lubin 2017/3/9
 * kafka数据异常恢复-新
 */
public class HandleKafkaByFileNew {

    private static final Logger logger = Logger.getLogger(HandleKafkaByFileNew.class);

    public static void main(String[] args) throws IOException {

        FileReader reader = new FileReader(args[0]);
        BufferedReader br = new BufferedReader(reader);

        ProducerUtil producer = new ProducerUtil();

        String str;

        while ((str = br.readLine()) != null) {
            String[] msg = str.split("\t");

            try {
                String topic = msg[1];
                String json = msg[5];

                producer.send(topic, UUID.randomUUID().toString().replaceAll("-", ""), json);
                System.out.println("成功.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("失败.");
                logger.error(str);
            }
        }

        br.close();
        reader.close();
        System.out.println("发送成功");

    }
}
