package com.easyim.service.common;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义客户端线程池工具类
 *
 * @author 单程车票
 */
public class ServiceThreadPoolExecutor {

    private static final String TAG = ServiceThreadPoolExecutor.class.getSimpleName();

    /**
     * CPU数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池核心线程数
     */
    private static final int CORE_POOL_SIZE = Math.max(2, Math.max(CPU_COUNT - 1, 4));

    /**
     * 线程池最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    /**
     * 非核心线程存货时间
     */
    private static final long KEEP_ALIVE_TIME = 30L;

    /**
     * 队列长度
     */
    private static final int QUEUE_LENGTH = 128;

    /**
     * 线程池
     */
    private static ThreadPoolExecutor executor = createThreadPoolExecutor();

    /**
     * 用于 UI 操作的线程池
     */
    private static final ExecutorService uiExecutor = Executors.newFixedThreadPool(
            CORE_POOL_SIZE, new ClientThreadFactory("UIClientThreadPool", 4));

    /**
     * 主线程引用
     */
    private static final Thread mainThread = Looper.getMainLooper().getThread();

    /**
     * 与主线程关联的 Handler
     */
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 用于记录后台等待的Runnable，第一个参数外面的Runnable，第二个参数是等待中的Runnable
     */
    private static final HashMap<Runnable, Runnable> mapToMainHandler = new HashMap<Runnable, Runnable>();

    private static ThreadPoolExecutor createThreadPoolExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(
                    CORE_POOL_SIZE,
                    MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE_TIME,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(QUEUE_LENGTH),
                    new ClientThreadFactory("ClientThreadPool", 3),
                    new ClientAbortPolicy());
        }
        return executor;
    }

    public static class ClientThreadFactory implements ThreadFactory {
        private final AtomicInteger cnt = new AtomicInteger(1);
        private String prefix = "";
        private int priority = Thread.NORM_PRIORITY;

        public ClientThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        public ClientThreadFactory(String prefix, int priority) {
            this.prefix = prefix;
            this.priority = priority;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread executor = new Thread(r, prefix + " #" + cnt.getAndIncrement());
            // 设置为守护线程（Android 应用通常通过守护线程来执行后台任务）
            executor.setDaemon(true);
            executor.setPriority(priority);
            return executor;
        }
    }

    private static class ClientAbortPolicy extends ThreadPoolExecutor.AbortPolicy {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            Log.d(TAG, "rejectedExecution:" + r);
            if (!executor.isShutdown()) {
                executor.shutdown();
                executor = null;
            }
            executor = createThreadPoolExecutor();
        }

    }

    /**
     * 启动一个消耗线程，常驻后台
     */
    public static void startConsumer(final Runnable r, final String name) {
        runInBackground(new Runnable() {
            public void run() {
                new ClientThreadFactory(name, 2).newThread(r).start();
            }
        });
    }

    /**
     * 执行后台任务
     */
    public static void runInBackground(Runnable runnable) {
        if (executor == null) {
            createThreadPoolExecutor();
        }
        executor.execute(runnable);
    }

    /**
     * 执行后台任务（延迟时间）
     */
    public static void runInBackground(final Runnable runnable, long delayMillis) {
        if (delayMillis <= 0) {
            runInBackground(runnable);
        } else {
            Runnable mainRunnable = new Runnable() {
                @Override
                public void run() {
                    mapToMainHandler.remove(runnable);
                    executor.execute(runnable);
                }
            };
            mapToMainHandler.put(runnable, mainRunnable);
            mainHandler.postDelayed(mainRunnable, delayMillis);
        }
    }

    /**
     * 提交UI任务
     */
    public static <T> Future<T> submitUITask(Callable<T> task) {
        return uiExecutor.submit(task);
    }

    /**
     * 强制清理任务
     */
    public static <T> void cancelTask(Future<T> task) {
        if (task != null) {
            task.cancel(true);
        }
    }

    /**
     * 从 Future 中获取值，如果发生异常，打日志
     */
    public static <T> T getFromTask(Future<T> future, String tag, String name) {
        try {
            return future.get();
        } catch (Exception e) {
            Log.e(tag, (name != null ? name + ": " : "") + e);
        }
        return null;
    }

    /**
     * 主线程执行任务
     */
    public static void runOnMainThread(Runnable r) {
        if (mainThread == Thread.currentThread()) {
            r.run();
        } else {
            mainHandler.post(r);
        }
    }

    /**
     * 主线程提交任务（延迟时间）
     */
    public static void runOnMainThread(Runnable r, long delayMillis) {
        if (delayMillis <= 0) {
            runOnMainThread(r);
        } else {
            mainHandler.postDelayed(r, delayMillis);
        }
    }

    /**
     * 用于移除主线程中等待执行的任务
     */
    public static void removeCallbackOnMainThread(Runnable r) {
        mainHandler.removeCallbacks(r);
    }

    /**
     * 用于移除后台线程中等待执行的任务
     */
    public static void removeCallbackInBackground(Runnable runnable) {
        Runnable mainRunnable = mapToMainHandler.get(runnable);
        if (mainRunnable != null) {
            mainHandler.removeCallbacks(mainRunnable);
        }
    }

}
