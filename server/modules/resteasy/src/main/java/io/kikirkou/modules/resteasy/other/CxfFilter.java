package io.kikirkou.modules.resteasy.other;

import io.kikirikou.modules.common.other.DelegatingSymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.jboss.resteasy.util.GetRestful;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public class CxfFilter extends FilterDispatcher {
    private final SymbolProvider symbolProvider;
    private final Collection<Object> configuration;


    public CxfFilter(SymbolProvider symbolProvider, Collection<Object> configuration) {
        this.symbolProvider = symbolProvider;
        this.configuration = configuration;
    }

    public void init(FilterConfig servletConfig) throws ServletException {
        SymbolProvider delegatingSymbolProvider = new DelegatingSymbolProvider(symbolProvider, servletConfig::getInitParameter);
        super.init(new FilterConfig() {
            @Override
            public String getFilterName() {
                return servletConfig.getFilterName();
            }

            @Override
            public ServletContext getServletContext() {
                return servletConfig.getServletContext();
            }

            @Override
            public String getInitParameter(String name) {
                return delegatingSymbolProvider.valueForSymbol(name);
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return servletConfig.getInitParameterNames();
            }
        });

        ArrayList<Object> resources = new ArrayList<>();
        ArrayList<Object> providers = new ArrayList<>();
        for (Object obj : configuration)
            if (GetRestful.isRootResource(obj.getClass()))
                resources.add(obj);
            else
                providers.add(obj);

        for (Object obj : providers)
            getDispatcher().getProviderFactory().registerProviderInstance(obj);
        for (Object obj : resources)
            getDispatcher().getRegistry().addSingletonResource(obj);
    }


}
