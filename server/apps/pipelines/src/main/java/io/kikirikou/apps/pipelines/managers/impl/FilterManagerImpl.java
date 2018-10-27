package io.kikirikou.apps.pipelines.managers.impl;

import io.kikirikou.apps.pipelines.managers.decl.FilterManager;
import io.kikirikou.apps.pipelines.other.FilterProcessor;

import java.util.Map;

public class FilterManagerImpl implements FilterManager {
    private final Map<String, FilterProcessor> configuration;

    public FilterManagerImpl(Map<String, FilterProcessor> configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean fitler(String filter, Object value) {
        String[] parts = filter.split(":");
        return configuration.get(parts[0]).apply(parts[1],value);
    }
}
