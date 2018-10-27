package io.kikirikou.modules.jetty.managers.impl.filters;


import io.kikirikou.modules.common.managers.decl.Environment;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EnvironmentFilter implements Filter {
    private final Environment environment;

    public EnvironmentFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        try {
            environment.push(HttpServletRequest.class, servletRequest);
            environment.push(HttpServletResponse.class, servletResponse);
            chain.doFilter(request, response);
        } finally {
            environment.pop(HttpServletResponse.class);
            environment.pop(HttpServletRequest.class);
        }
    }

    @Override
    public void destroy() {
    }
}