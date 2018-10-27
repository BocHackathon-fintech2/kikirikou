package io.kikirikou.modules.bootstrap.managers.impl;

import io.kikirikou.modules.bootstrap.managers.decl.SignalManager;
import io.kikirikou.modules.bootstrap.other.SignalHandler;
import sun.misc.Signal;

import java.util.Map;

public class SignalManagerImpl implements SignalManager {

    public SignalManagerImpl(Map<String, SignalHandler> configuration) {
        configuration.forEach(this::register);
    }


    @Override
    public void register(String signal, SignalHandler handler) {
        Signal.handle(new Signal(signal), handler::accept);
    }
}
