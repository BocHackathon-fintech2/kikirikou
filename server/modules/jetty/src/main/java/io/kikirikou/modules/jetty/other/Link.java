package io.kikirikou.modules.jetty.other;

import okhttp3.HttpUrl;

public class Link {
    private final String baseUrl;
    private final HttpUrl url;

    public Link(String baseUrl, HttpUrl url) {
        this.baseUrl = baseUrl;
        this.url = url;
    }

    public String toAbsolute() {
        return url.toString();
    }

    public String toRelative() {
        return toAbsolute().substring(baseUrl.length());
    }
}
