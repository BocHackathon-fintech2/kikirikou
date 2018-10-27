package io.kikirkou.modules.resteasy.managers.impl;

import io.kikirikou.modules.common.other.DelegatingSymbolProvider;
import io.kikirikou.modules.common.other.SingleKeySymbolProvider;
import io.kikirikou.modules.jetty.other.JettyHandlerCustomizer;
import io.kikirkou.modules.resteasy.managers.decl.RestEasyResourceLocator;
import io.kikirkou.modules.resteasy.other.CxfFilter;
import io.kikirkou.modules.resteasy.other.RestEasySymbolConstants;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class JettyRestEasyCustomizer implements JettyHandlerCustomizer {
    private static final String ADD_CHARSET_SYMBOL = "resteasy.add.charset";

    private final RestEasyResourceLocator restEasyResourceLocator;
    private final SymbolProvider symbolProvider;
    private final String mappingPrefix;

    public JettyRestEasyCustomizer(SymbolSource symbolSource,
                                   RestEasyResourceLocator restEasyResourceLocator,
                                   @Symbol(RestEasySymbolConstants.MAPPING_PREFIX) String mappingPrefix) {
        this.restEasyResourceLocator = restEasyResourceLocator;
        this.mappingPrefix = mappingPrefix;
        this.symbolProvider = new DelegatingSymbolProvider(
                new SingleKeySymbolProvider(ADD_CHARSET_SYMBOL, Boolean.FALSE.toString()),
                new SingleKeySymbolProvider(ResteasyContextParameters.RESTEASY_SERVLET_MAPPING_PREFIX, mappingPrefix),
                symbolName -> {
                    try {
                        return symbolSource.valueForSymbol(symbolName);
                    } catch (Throwable ignored) {
                        return null;
                    }
                });
    }

    @Override
    public void customize(ServletContextHandler handler) {
        FilterHolder fh = new FilterHolder(new CxfFilter(symbolProvider, restEasyResourceLocator.getResources()));
        handler.addFilter(fh, mappingPrefix + "/*", EnumSet.allOf(DispatcherType.class));
    }
}
