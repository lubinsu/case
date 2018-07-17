package com.maiya;

import com.alibaba.fastjson.JSON;
import com.maiya.kafka.consumer.utils.ProducerUtil;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by lubin 2017/1/10
 */
public class HandleKafkaByFile {

    private static final Logger logger = Logger.getLogger(HandleKafkaByFile.class);

    public static void main(String[] args) throws IOException {

        FileReader reader = new FileReader(args[0]);
        BufferedReader br = new BufferedReader(reader);

        ProducerUtil producer = new ProducerUtil();

        String str;

        while ((str = br.readLine()) != null) {

            try {
                String msg = str.substring(str.lastIndexOf("【该条数据入库异常】") + 10);
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(msg);

                String table_name = jsonObject.getString("table_name").toLowerCase();

                producer.send(table_name, UUID.randomUUID().toString().replaceAll("-", ""), msg);
                System.out.println("成功.");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(str);
            }
        }

        br.close();
        reader.close();
        System.out.println("发送成功");

    }
}
