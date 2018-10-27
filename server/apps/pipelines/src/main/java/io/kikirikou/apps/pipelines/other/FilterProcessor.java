package io.kikirikou.apps.pipelines.other;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FilterProcessor extends BiFunction<String,Object,Boolean> {
}
