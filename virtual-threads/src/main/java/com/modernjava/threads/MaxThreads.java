package com.modernjava.threads;


import com.modernjava.util.CommonUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.modernjava.util.LoggerUtil.log;

public class MaxThreads {

    public static final int MAX_THREADS = 17000;
    static AtomicInteger atomicInteger = new AtomicInteger();

    public static void doSomeWork(int index) {
        log("started doSomeWork : " + index);
        //Any task that's started by a thread is blocked until it completes.
        //It could be any IO call such as HTTP or File IO call.
        CommonUtil.sleep(5000);
        log("finished doSomeWork : " + index);
    }

    public static void main(String[] args) {

        IntStream.rangeClosed(1, MAX_THREADS)
                        .forEach(i -> {
                            Thread.ofPlatform().start(() -> doSomeWork(i));
                        });
        log("Program Completed!");
    }

}
