package com.maiya.util.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * Created by lubin 2016/11/30
 */
public class CheckJobStatus {

    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet get = new HttpGet("http://hadoop02:8088/ws/v1/cluster/apps?states=RUNNING");

            CloseableHttpResponse httpResponse;
            //发送get请求
            httpResponse = httpClient.execute(get);
            try {
                //response实体
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity) {
                    System.out.println("响应状态码:" + httpResponse.getStatusLine());
                    String retSrc = EntityUtils.toString(entity);
                    JSONObject json = JSONObject.parseObject(retSrc);

                    JSONArray array = json.getJSONObject("apps").getJSONArray("app");
                    for (int i = 0; i < array.size(); i++) {
                        System.out.println(array.getJSONObject(i).getString("name"));
                    }
                }
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
