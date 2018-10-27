package io.kikirikou.modules.jetty.managers.impl;

import io.kikirikou.modules.common.managers.decl.Environment;
import io.kikirikou.modules.common.other.CommonSymbolConstants;
import io.kikirikou.modules.jetty.managers.decl.JettyManager;
import io.kikirikou.modules.jetty.managers.decl.ResourceSource;
import io.kikirikou.modules.jetty.managers.impl.filters.CleanupThreadFilter;
import io.kikirikou.modules.jetty.managers.impl.filters.EnvironmentFilter;
import io.kikirikou.modules.jetty.other.JettyHandlerCustomizer;
import io.kikirikou.modules.jetty.other.JettySymbolConstants;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;


public class JettyManagerImpl implements JettyManager, Runnable {
    private final Server server;

    public JettyManagerImpl(RegistryShutdownHub registryShutdownHub,
                            ResourceSource resourceSource,
                            List<JettyHandlerCustomizer> configuration,
                            PerthreadManager perthreadManager,
                            Environment environment,
                            @Symbol(JettySymbolConstants.BIND_PORT) int port,
                            @Symbol(JettySymbolConstants.BIND_ADDRESS) String bindAddress,
                            @Symbol(JettySymbolConstants.ASSETS_PREFIX) String assetsPrefix,
                            @Symbol(JettySymbolConstants.MAX_FORM_CONTENT_IN_BYTES) int maxFormContentInBytes,
                            @Symbol(CommonSymbolConstants.PRODUCTION_MODE) boolean productionMode) {
        this.server = new Server();

        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.addCustomizer(new org.eclipse.jetty.server.ForwardedRequestCustomizer());

        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);
        ServerConnector connector = new ServerConnector(server, httpConnectionFactory);
        connector.setPort(port);
        connector.setHost(bindAddress);

        ServletContextHandler assetsContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        assetsContext.setBaseResource(resourceSource.getResources());
        assetsContext.setContextPath(assetsPrefix);

        ServletContextHandler filtersContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        filtersContext.setContextPath("/");
        filtersContext.setMaxFormContentSize(maxFormContentInBytes);

        FilterHolder cleanupThreadFilterHolder = new FilterHolder(new CleanupThreadFilter(perthreadManager));
        filtersContext.addFilter(cleanupThreadFilterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));

        FilterHolder environmentFilterHolder = new FilterHolder(new EnvironmentFilter(environment));
        filtersContext.addFilter(environmentFilterHolder, "/*", EnumSet.allOf(DispatcherType.class));

        configuration.forEach(jettyHandlerCustomizer -> jettyHandlerCustomizer.customize(filtersContext));
        server.setConnectors(new ServerConnector[]{connector});

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{filtersContext, assetsContext});

        if (!productionMode) {
            GzipHandler gzip = new GzipHandler();
            gzip.setIncludedMethods("GET", "POST");
            gzip.setHandler(contexts);

            server.setHandler(gzip);
        } else
            server.setHandler(contexts);

        ServletHolder holderDef = new ServletHolder("default", DefaultServlet.class);
        holderDef.setInitParameter("dirAllowed", Boolean.valueOf(!productionMode).toString());
        holderDef.setInitParameter("etags", Boolean.TRUE.toString());
        assetsContext.addServlet(holderDef, "/");

        registryShutdownHub.addRegistryShutdownListener(this);

    }

    @Override
    public void startup() {
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            server.stop();
        } catch (Exception ignored) {
        }
    }
}
