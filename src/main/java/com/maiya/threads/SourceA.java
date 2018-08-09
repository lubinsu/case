package com.maiya.threads;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lubinsu
 * Date: 2018/8/9 10:50
 * Desc:
 */
public class SourceA {
    private List<String> list = new ArrayList<>();

    public synchronized void getSource() {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public synchronized void setSource(String id) {
        list.add(id);
    }
}
