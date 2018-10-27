package io.kikirikou.modules.common.managers.decl;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.function.Consumer;

public interface FileMonitor {
    Runnable monitor(Path path, Consumer<WatchEvent<Path>> listener, WatchEvent.Kind<?>... events);
}
