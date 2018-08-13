package com.maiyabank;

import com.maiya.threads.SourceA;
import com.maiya.threads.TestRunnable;
import com.maiya.threads.TestThread;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

import com.maiya.lambda.*;

import static java.lang.Thread.sleep;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testSort() {
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        names.sort(String::compareTo);
        names.forEach(System.out::println);
    }

    public void testInt() {
        Converter<Integer, String> converter = Integer::valueOf;
    }

    public void testOptional() {
        Optional<String> optional = Optional.empty();
        Optional<String> optional2 = Optional.of("hello");
        System.out.println(optional.orElse("ddd"));
    }

    public void testStreams() {
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        stringCollection
                .stream()
                .sorted()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);

    }

    public void testReduce() {

        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        Optional<String> reduced =
                stringCollection
                        .stream()
                        .sorted()
                        .reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(System.out::println);
    }

    public void test() throws InterruptedException {

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        SourceA s = new SourceA();
        TestThread tt = new TestThread(s, countDownLatch);
        TestRunnable tr = new TestRunnable(s, countDownLatch);
        Thread t = new Thread(tr);
        System.out.println("线程1状态：" + t.getState());
        System.out.println("线程2状态：" + tt.getState());
        System.out.println("调用线程2");
        tt.start();
        System.out.println("线程1状态：" + t.getState());
        System.out.println("线程2状态：" + tt.getState());
        System.out.println("调用线程1");
        t.start();
        System.out.println("线程1状态：" + t.getState());
        System.out.println("线程2状态：" + tt.getState());

        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("线程1状态：" + t.getState());
                System.out.println("线程2状态：" + tt.getState());
                sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        countDownLatch.await();
    }
}
