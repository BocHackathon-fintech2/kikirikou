package io.kikirikou.modules.http.managers.impl;

import io.kikirikou.modules.common.managers.decl.StringToDataSourceProcessor;
import io.kikirikou.modules.common.other.DataSource;
import io.kikirikou.modules.http.other.AssetDownloadDataSource;
import okhttp3.OkHttpClient;

public class HttpProcessor implements StringToDataSourceProcessor {
    private final OkHttpClient httpClient;

    public HttpProcessor(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public DataSource process(String scheme, String value) {
        return new AssetDownloadDataSource(httpClient, String.format("%s://%s", scheme, value));
    }
}
