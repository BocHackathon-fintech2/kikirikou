package io.kikirikou.modules.jetty.managers.impl;

import io.kikirikou.modules.jetty.managers.decl.BaseURLSource;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseURLSourceImpl implements BaseURLSource {
    private final HttpServletRequest httpServletRequest;

    public BaseURLSourceImpl(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getBaseURL() {
        boolean secure = httpServletRequest.isSecure();
        return String.format("%s://%s%s",
                secure ? "https" : "http",
                httpServletRequest.getServerName(),
                portExtension(secure));
    }

    private String portExtension(boolean secure) {
        int configuredPort = httpServletRequest.getServerPort();
        int expectedPort = secure ? 443 : 80;

        if (configuredPort == expectedPort)
            return StringUtils.EMPTY;

        return ":" + configuredPort;
    }
}
