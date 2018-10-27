package io.kikirikou.modules.common.managers.impl;


import io.kikirikou.modules.common.managers.decl.StringToDataSourceManager;
import io.kikirikou.modules.common.managers.decl.StringToDataSourceProcessor;
import io.kikirikou.modules.common.other.DataSource;
import io.kikirikou.modules.common.utils.AssertUtils;

import java.util.Map;

public class StringToDataSourceManagerImpl implements StringToDataSourceManager {

    private final Map<String, StringToDataSourceProcessor> configuration;

    public StringToDataSourceManagerImpl(Map<String, StringToDataSourceProcessor> configuration) {
        this.configuration = configuration;
    }


    @Override
    public DataSource get(String url) {
        AssertUtils.hasLength(url, "url");
        int i = url.indexOf(StringToDataSourceManager.SCHEME_SEPARATOR);
        String scheme = url;
        if (i >= 0)
            scheme = url.substring(0, i);

        if (!configuration.containsKey(scheme))
            throw new RuntimeException(String.format("No support for scheme: %s found", scheme));

        return configuration.get(scheme).
                process(scheme, url.substring(i + StringToDataSourceManager.SCHEME_SEPARATOR.length()));
    }
}
