package com.maiya;

import com.maiya.kafka.consumer.utils.ProducerUtil;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by lubin 2016/12/27
 */
public class SendKafka {
    public static void main(String[] args) throws IOException {

        ProducerUtil producer = new ProducerUtil();

        String topic = args[0];
        String str = args[1];

        producer.send(topic, UUID.randomUUID().toString().replaceAll("-", ""), str);

    }
}
