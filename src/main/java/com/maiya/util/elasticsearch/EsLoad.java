package com.maiya.util.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.maiya.beans.MemberContactsES;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by lubin 2017/3/1
 */
public class EsLoad {

    public static void main(String[] args) throws IOException {

        Settings settings = Settings.builder().put("cluster.name", "my-application").build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("master"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("slaver01"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("slaver02"), 9300));

        BufferedReader br = new BufferedReader(new FileReader("E:\\download\\000001_1"));
        String json;
        int count = 0;
        //开启批量插入

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        while ((json = br.readLine()) != null) {
            String[] field = json.split("\t");
            if (field.length == 9) {
                MemberContactsES memberContactsES = new MemberContactsES(field[0], field[1], field[2], field[3], field[4], field[5], field[6], field[7], field[8]);
                bulkRequest.add(client.prepareIndex("myd", "hy_membercontacts").setId(field[0]).setSource(JSON.toJSON(memberContactsES).toString()));
                //每一千条提交一次
                if (count % 1000 == 0) {
                    bulkRequest.execute().actionGet();

                    System.out.println("提交了：" + count);
                }
                count++;
                if (count == 1000) {
                    break;
                }
            }
        }
        bulkRequest.execute().actionGet();
        System.out.println("插入完毕");
        br.close();
        client.close();
    }
}
