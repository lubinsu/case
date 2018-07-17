package com.maiya;

/**
 * Created by lubin 2016/11/30
 */
public class HelloWorld {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void printHello() {
        System.out.println("Spring 3 : CheckJobStatus ! " + name);
    }
}
