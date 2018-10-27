package io.kikirikou.modules.bootstrap.other;

import sun.misc.Signal;

import java.util.function.Consumer;

@FunctionalInterface
public interface SignalHandler extends Consumer<Signal> {
}
