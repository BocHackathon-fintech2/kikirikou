package io.kikirikou.modules.common.other;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class AbstractEnum {

    private static final ConcurrentHashMap<Entry<Class, String>, Object> VALUES = new ConcurrentHashMap<>();

    private final String value;

    protected AbstractEnum(String value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends AbstractEnum> T value(String value, Class<T> clz, Function<String, T> creator) {
        return (T) VALUES.computeIfAbsent(new SimpleImmutableEntry<>(clz, value), ignored -> creator.apply(value));
    }

    public final String value() {
        return this.value;
    }

    @Override
    public final int hashCode() {
        return 73 * getClass().hashCode() * value.hashCode();
    }

    @Override
    public final boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        return other.getClass().equals(this.getClass()) && ((AbstractEnum) other).value.equals(this.value);
    }

    @Override
    public final String toString() {
        return value;
    }
}