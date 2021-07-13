package com.neoniou.bot.utils;

import java.util.concurrent.*;

/**
 * @author Neo.Zzj
 * @date 2020/12/17
 */
public class ThreadUtil {

    public static ExecutorService createSingle() {
        return new ThreadPoolExecutor(1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
