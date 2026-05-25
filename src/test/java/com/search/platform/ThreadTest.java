package com.search.platform;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.tomcat.util.threads.TaskThread;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class ThreadTest {
    @Test
    public void TestThread() throws ExecutionException, InterruptedException {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                System.out.println("t1线程启动了");
            }
        };
        Runnable r1 = () ->{
            System.out.println("t2线程启动了");
        };

        t1.start();
        Thread t2 = new Thread(r1);
        t2.start();

        MyCallAble myCallAble = new MyCallAble();
        FutureTask task = new FutureTask(myCallAble);
        Thread t3 = new Thread(task);
        t3.start();
        String str = task.get().toString();
        System.out.println(str);
    }
    class MyCallAble implements Callable{

        @Override
        public Object call() throws Exception {

            return Thread.currentThread().getName() + "hello world";
        };
    }
}
