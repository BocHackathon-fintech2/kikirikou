package io.kikirikou.modules.jetty.modules;

import io.kikirikou.modules.common.managers.decl.EnvironmentalShadowBuilder;
import io.kikirikou.modules.common.managers.decl.StartupManager;
import io.kikirikou.modules.http.modules.HttpModule;
import io.kikirikou.modules.jetty.managers.decl.AssetLinkSource;
import io.kikirikou.modules.jetty.managers.decl.BaseURLSource;
import io.kikirikou.modules.jetty.managers.decl.JettyManager;
import io.kikirikou.modules.jetty.managers.decl.ResourceSource;
import io.kikirikou.modules.jetty.managers.impl.AssetLinkSourceImpl;
import io.kikirikou.modules.jetty.managers.impl.BaseURLSourceImpl;
import io.kikirikou.modules.jetty.managers.impl.JettyManagerImpl;
import io.kikirikou.modules.jetty.managers.impl.ResourceSourceImpl;
import io.kikirikou.modules.jetty.managers.startup.StartJetty;
import io.kikirikou.modules.jetty.other.JettySymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ImportModule(HttpModule.class)
public class JettyModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(JettyManager.class, JettyManagerImpl.class);
        binder.bind(BaseURLSource.class, BaseURLSourceImpl.class);
        binder.bind(ResourceSource.class, ResourceSourceImpl.class);
        binder.bind(AssetLinkSource.class, AssetLinkSourceImpl.class);
    }

    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(JettySymbolConstants.BIND_PORT, 8080);
        configuration.add(JettySymbolConstants.BIND_ADDRESS, "127.0.0.1");
        configuration.add(JettySymbolConstants.ASSETS_PREFIX, "/assets");
        configuration.add(JettySymbolConstants.MAX_FORM_CONTENT_IN_BYTES, 1000000); //1Mb
    }

    @Contribute(StartupManager.class)
    public static void contributeStartupManager(OrderedConfiguration<Runnable> configuration) {
        configuration.addInstance("startJetty", StartJetty.class);
    }

    public static HttpServletRequest buildHttpServletRequest(EnvironmentalShadowBuilder environmentalShadowBuilder) {
        return environmentalShadowBuilder.build(HttpServletRequest.class);
    }

    public static HttpServletResponse buildHttpServletResponse(EnvironmentalShadowBuilder environmentalShadowBuilder) {
        return environmentalShadowBuilder.build(HttpServletResponse.class);
    }
}
