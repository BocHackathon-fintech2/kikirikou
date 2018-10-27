package io.kikirikou.modules.jetty.managers.impl.filters;

import org.apache.tapestry5.ioc.services.PerthreadManager;

import javax.servlet.*;
import java.io.IOException;

public class CleanupThreadFilter implements Filter {
    private final PerthreadManager perthreadManager;

    public CleanupThreadFilter(PerthreadManager perthreadManager) {
        this.perthreadManager = perthreadManager;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            perthreadManager.cleanup();
        }
    }

    @Override
    public void destroy() {
    }
}