package com.modernjava.virtualthreads;


import com.modernjava.util.CommonUtil;

import static com.modernjava.util.LoggerUtil.log;

public class ExploreVirtualThreads {

    public static void doSomeWork() {
        log("started doSomeWork");
        CommonUtil.sleep(1000);
        log("finished doSomeWork");
    }

    public static void main(String[] args) {
        var thread1 = Thread.ofVirtual().name("t1");
        var thread2 = Thread.ofVirtual().name("t2");
        thread1.start(() -> log("Run task 1 in the background"));
        thread2.start(ExploreVirtualThreads::doSomeWork);
        log("Program Completed!");
        CommonUtil.sleep(2000);
    }

}
