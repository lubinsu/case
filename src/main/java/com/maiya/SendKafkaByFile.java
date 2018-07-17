package com.maiya;

import com.maiya.kafka.consumer.utils.ProducerUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by lubin 2017/1/10
 */
public class SendKafkaByFile {
    public static void main(String[] args) throws IOException {

        FileReader reader = new FileReader(args[0]);
        BufferedReader br = new BufferedReader(reader);

        ProducerUtil producer = new ProducerUtil();

        String str;

        while ((str = br.readLine()) != null) {
            producer.send(str.split("`")[0], UUID.randomUUID().toString().replaceAll("-", ""), str.split("`")[1]);
        }

        br.close();
        reader.close();
        System.out.println("发送成功");
    }
}
