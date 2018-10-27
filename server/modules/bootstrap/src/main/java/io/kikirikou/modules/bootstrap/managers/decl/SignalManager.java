package io.kikirikou.modules.bootstrap.managers.decl;

import io.kikirikou.modules.bootstrap.other.SignalHandler;

public interface SignalManager {
    void register(String signal, SignalHandler handler);
}
