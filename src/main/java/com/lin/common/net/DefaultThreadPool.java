package com.lin.common.net;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * Created by linweilin on 2016/11/8.
 */

public class DefaultThreadPool {

    static final int BLOCKING_QUEUE_SIZE = 20;  // 阻塞队列最大任务数量
    static final int THREAD_POOL_MAX_SIZE = 10; // 最大任务数量
    static final int THREAD_POOL_SIZE = 6;

    //缓冲的请求任务队列
    static ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(
            DefaultThreadPool.BLOCKING_QUEUE_SIZE);

    private static DefaultThreadPool instance = null;

    static AbstractExecutorService pool = new ThreadPoolExecutor(
            THREAD_POOL_SIZE,THREAD_POOL_MAX_SIZE,15L,
            TimeUnit.SECONDS,blockingQueue,
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    public static synchronized DefaultThreadPool getInstance(){
        if (instance == null){
            instance = new DefaultThreadPool();
        }
        return instance;
    }

    public static void removeAllTask(){
        blockingQueue.clear();
    }

    public static void removeTaskFromQueue(final Object o){
        blockingQueue.remove(o);
    }

    public static void shutdown(){
        if (pool != null){
            pool.shutdown();
        }
    }

    public static void shutdownRightnow(){
        if (pool != null){
            pool.shutdown();
            try {
                pool.awaitTermination(1,TimeUnit.MICROSECONDS);
            }catch (final InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void execute(final Runnable r){
        if (r != null){
            try{
                pool.execute(r);
            } catch (final Exception e){
                e.printStackTrace();
            }
        }
    }
}
