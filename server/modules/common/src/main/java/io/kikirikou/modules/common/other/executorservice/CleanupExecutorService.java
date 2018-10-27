package io.kikirikou.modules.common.other.executorservice;

import org.apache.tapestry5.ioc.services.PerthreadManager;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CleanupExecutorService implements ExecutorService {
    private final ExecutorService delegate;
    private final PerthreadManager perthreadManager;

    public CleanupExecutorService(ExecutorService delegate, PerthreadManager perthreadManager) {
        this.delegate = delegate;
        this.perthreadManager = perthreadManager;
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        return delegate.awaitTermination(l, timeUnit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return delegate.submit(wrap(callable));
    }

    @Override
    public <T> Future<T> submit(Runnable runnable, T t) {
        return delegate.submit(wrap(runnable), t);
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return delegate.submit(wrap(runnable));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        return delegate.invokeAll(wrap(collection));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
        return delegate.invokeAll(wrap(collection), l, timeUnit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        return delegate.invokeAny(wrap(collection));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.invokeAny(wrap(collection), l, timeUnit);
    }

    @Override
    public void execute(Runnable runnable) {
        delegate.execute(wrap(runnable));
    }

    private <T> Callable<T> wrap(Callable<T> callable) {
        return () -> {
            try {
                return callable.call();
            } finally {
                perthreadManager.cleanup();
            }
        };
    }

    private <T> Collection<Callable<T>> wrap(Collection<? extends Callable<T>> collection) {
        return collection.stream().map((Function<Callable<T>, Callable<T>>) this::wrap).collect(Collectors.toList());
    }


    private Runnable wrap(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } finally {
                perthreadManager.cleanup();
            }
        };
    }
}
