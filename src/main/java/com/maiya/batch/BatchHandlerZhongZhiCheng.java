package com.maiya.batch;

import com.alibaba.fastjson.JSONObject;
import com.maiya.util.comm.CommUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lubinsu
 * Date: 2018/5/24 16:20
 * Desc:
 */
public class BatchHandlerZhongZhiCheng extends Thread {


    private String params;
    private String url;
    private String sUserid;

    public BatchHandlerZhongZhiCheng(String params, String url, String sUserid) {
        this.params = params;
        this.url = url;
        this.sUserid = sUserid;
    }

    public void run() {
        String resJson = CommUtil.httpPost(url, params);
        JSONObject res = JSONObject.parseObject(resJson);
        try {
            String result = res.getString("result");
            System.out.println(sUserid.concat("\t").concat(result));
        } catch (Exception e) {
            System.out.println(resJson);
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {

        String path = args[0];
        int nThreads = Integer.valueOf(args[1]);

        //创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        FileReader reader = new FileReader(path);
        BufferedReader br = new BufferedReader(reader);

        String str;
        while ((str = br.readLine()) != null) {
            String paramStr = "{\n" +
                    "  \"device\": {\n" +
                    "    \"browser_name\": null,\n" +
                    "    \"browser_version\": null,\n" +
                    "    \"device_type\": null,\n" +
                    "    \"gps_latitude\": null,\n" +
                    "    \"gps_longitude\": null,\n" +
                    "    \"hd_serial_number\": null,\n" +
                    "    \"idfa\": null,\n" +
                    "    \"idfv\": null,\n" +
                    "    \"imei\": null,\n" +
                    "    \"ip_address\": null,\n" +
                    "    \"keychain_uuid\": null,\n" +
                    "    \"mac\": null,\n" +
                    "    \"os_name\": null,\n" +
                    "    \"os_version\": null,\n" +
                    "    \"serial_number\": null\n" +
                    "  },\n" +
                    "  \"loan_type\": \"2\",\n" +
                    "  \"mobile\": null,\n" +
                    "  \"name\": null,\n" +
                    "  \"pid\": null,\n" +
                    "  \"sub_tenant\": null\n" +
                    "}";
            String[] infos = str.split("\t");
            String sUserId = infos[0];
            String sidno = infos[1];
            String smobile = infos[2];
            String sname = infos[3];
            JSONObject json = JSONObject.parseObject(paramStr);
            json.put("mobile", smobile);
            json.put("name", sname);
            json.put("pid", sidno);

            BatchHandlerZhongZhiCheng batchHandler = new BatchHandlerZhongZhiCheng(json.toJSONString(), "http://171.16.10.176:8091/gateway/queryZhongZhiCheng", sUserId);
//            BatchHandlerZhongZhiCheng batchHandler = new BatchHandlerZhongZhiCheng(json.toJSONString(), "http://192.168.0.119:8091/gateway/queryZhongZhiCheng", sUserId);
            pool.execute(batchHandler);
        }
        br.close();
        reader.close();

        //关闭线程池
        pool.shutdown();
    }

}
