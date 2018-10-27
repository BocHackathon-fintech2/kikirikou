package io.kikirikou.modules.common.managers.impl;

import io.kikirikou.modules.common.managers.decl.FileMonitor;
import io.kikirikou.modules.common.utils.AssertUtils;
import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ParallelExecutor;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.function.Consumer;

public class FileMonitorImpl implements FileMonitor, Invokable<Void> {
    private final WatchService watchService;
    private final Map<WatchKey, Consumer<WatchEvent<Path>>> listeners;
    private volatile boolean shutDown = false;

    public FileMonitorImpl(ParallelExecutor parallelExecutor, RegistryShutdownHub registryShutdownHub) throws IOException {
        this.listeners = CollectionFactory.newConcurrentMap();
        watchService = FileSystems.getDefault().newWatchService();
        registryShutdownHub.addRegistryShutdownListener((Runnable) () -> {
            shutDown = true;
            try {
                watchService.close();
            } catch (Exception ignored) {
            }
        });
        parallelExecutor.invoke(this);
    }

    @Override
    public Runnable monitor(Path path, Consumer<WatchEvent<Path>> listener, WatchEvent.Kind<?>... events) {
        AssertUtils.notNull(path, "path");
        AssertUtils.assertThat(Files.isDirectory(path), "Path is not directory");
        try {
            WatchKey watchKey = path.register(watchService, events);
            listeners.put(watchKey, listener);
            return new CancellableRunnable(watchKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Void invoke() {
        while (!shutDown) {
            WatchKey watchKey;
            try {
                watchKey = watchService.take();
            } catch (ClosedWatchServiceException | InterruptedException e) {
                break;
            }

            if (!watchKey.isValid())
                continue;

            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                listeners.get(watchKey).accept(ev);
                watchKey.reset();
            }

        }

        return null;
    }

    private class CancellableRunnable implements Runnable {
        private final WatchKey watchKey;

        private CancellableRunnable(WatchKey watchKey) {
            this.watchKey = watchKey;
        }

        @Override
        public void run() {
            AssertUtils.assertThat(listeners.containsKey(watchKey), "Monitor not valid");
            listeners.remove(watchKey);
            watchKey.cancel();
        }
    }

}
