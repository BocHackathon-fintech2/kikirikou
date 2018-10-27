package io.kikirikou.modules.common.other.executorservice;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class DirectExecutionService extends AbstractExecutorService {
    private final Object lock = new Object();
    private int runningTasks = 0;
    private boolean shutdown = false;


    @Override
    public void shutdown() {
        synchronized (lock) {
            shutdown = true;
            if (runningTasks == 0) {
                lock.notifyAll();
            }
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        shutdown();
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        synchronized (lock) {
            return shutdown && runningTasks == 0;
        }
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(l);
        synchronized (lock) {
            for (; ; ) {
                if (shutdown && runningTasks == 0)
                    return true;
                else if (nanos <= 0) {
                    return false;
                } else {
                    long now = System.nanoTime();
                    TimeUnit.NANOSECONDS.timedWait(lock, nanos);
                    nanos -= System.nanoTime() - now;  // subtract the actual time we waited
                }
            }
        }
    }

    @Override
    public void execute(Runnable runnable) {
        startTask();
        try {
            runnable.run();
        } finally {
            endTask();
        }
    }

    private void startTask() {
        synchronized (lock) {
            if (shutdown)
                throw new RejectedExecutionException("Executor already shutdown");
            runningTasks++;
        }
    }

    private void endTask() {
        synchronized (lock) {
            int numRunning = --runningTasks;
            if (numRunning == 0)
                lock.notifyAll();
        }
    }
}
