package com.esayim.client.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自定义客户端线程池
 *
 * @author 单程车票
 */
public class ClientExecutorService {

    /**
     * 重连线程池
     */
    private ExecutorService reconnectPool;

    /**
     * 心跳维持线程池
     */
    private ExecutorService heartbeatPool;

    /**
     * 初始化重连线程池
     */
    public synchronized void initReconnectPool() {
        initReconnectPool(1);
    }

    /**
     * 初始化重连线程池
     *
     * @param size 线程池大小
     */
    public synchronized void initReconnectPool(int size) {
        destroyReconnectPool();
        reconnectPool = Executors.newFixedThreadPool(size);
    }

    /**
     * 初始化心跳维持线程池
     */
    public synchronized void initHeartbeatPool() {
        initHeartbeatPool(1);
    }

    /**
     * 初始化work线程池
     *
     * @param size 线程池大小
     */
    public synchronized void initHeartbeatPool(int size) {
        destroyHeartbeatPool();
        heartbeatPool = Executors.newFixedThreadPool(size);
    }

    /**
     * 执行重连任务
     *
     * @param r 任务
     */
    public void execReconnectTask(Runnable r) {
        if (reconnectPool == null) {
            initReconnectPool();
        }
        reconnectPool.execute(r);
    }

    /**
     * 执行心跳维持任务
     *
     * @param r 任务
     */
    public void execHeartbeatTask(Runnable r) {
        if (heartbeatPool == null) {
            initHeartbeatPool();
        }
        heartbeatPool.execute(r);
    }

    /**
     * 释放boss线程池
     */
    public synchronized void destroyReconnectPool() {
        if (reconnectPool != null) {
            try {
                reconnectPool.shutdownNow();
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                reconnectPool = null;
            }
        }
    }

    /**
     * 释放work线程池
     */
    public synchronized void destroyHeartbeatPool() {
        if (heartbeatPool != null) {
            try {
                heartbeatPool.shutdownNow();
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                heartbeatPool = null;
            }
        }
    }

    /**
     * 释放所有线程池
     */
    public synchronized void destroy() {
        destroyReconnectPool();
        destroyHeartbeatPool();
    }
}
