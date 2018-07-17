package com.maiya.batch;

import com.maiya.util.comm.CommUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lubinsu
 * Date: 2018/5/24 16:20
 * Desc:
 */
public class BatchHandler extends Thread {


    private String params;
    private String url;

    public BatchHandler(String params, String url) {
        this.params = params;
        this.url = url;
    }

    public void run() {
        String resJson = CommUtil.httpPost(url, params);
        System.out.println(resJson);
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
            BatchHandler batchHandler = new BatchHandler(str.split("`")[0], str.split("`")[1]);
            pool.execute(batchHandler);
        }
        br.close();
        reader.close();

        //关闭线程池
        pool.shutdown();
    }

}
