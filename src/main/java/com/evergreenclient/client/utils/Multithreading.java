/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Multithreading {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final ScheduledExecutorService RUNNABLE_POOL = Executors.newScheduledThreadPool(10, (r) -> new Thread(r, "Evergreen Thread " + counter.incrementAndGet()));
    public static ThreadPoolExecutor POOL = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), (r) -> new Thread(r, String.format("Thread %s", counter.incrementAndGet())));

    public static ScheduledFuture<?> schedule(Runnable r, long initialDelay, long delay, TimeUnit unit) {
        return RUNNABLE_POOL.scheduleAtFixedRate(r, initialDelay, delay, unit);
    }

    public static ScheduledFuture<?> schedule(Runnable r, long delay, TimeUnit unit) {
        return RUNNABLE_POOL.schedule(r, delay, unit);
    }

    public static void runAsync(Runnable runnable) {
        POOL.execute(runnable);
    }

    public static Future<?> submit(Runnable runnable) {
        return POOL.submit(runnable);
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
