package io.kikirikou.modules.jetty.other;

import org.eclipse.jetty.servlet.ServletContextHandler;

public interface JettyHandlerCustomizer {
    void customize(ServletContextHandler handler);
}
