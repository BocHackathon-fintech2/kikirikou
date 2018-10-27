package io.kikirkou.modules.resteasy.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.kikirikou.modules.common.other.CommonSymbolConstants;
import io.kikirikou.modules.jetty.managers.decl.JettyManager;
import io.kikirikou.modules.jetty.modules.JettyModule;
import io.kikirikou.modules.jetty.other.JettyHandlerCustomizer;
import io.kikirkou.modules.resteasy.managers.decl.RestEasyResourceLocator;
import io.kikirkou.modules.resteasy.managers.decl.RestResourceLinkSource;
import io.kikirkou.modules.resteasy.managers.impl.JettyRestEasyCustomizer;
import io.kikirkou.modules.resteasy.managers.impl.RestEasyResourceLocatorImpl;
import io.kikirkou.modules.resteasy.managers.impl.RestResourceLinkSourceImpl;
import io.kikirkou.modules.resteasy.other.RestEasySymbolConstants;
import io.kikirkou.modules.resteasy.rest.GsonMessageBodyHandler;
import io.kikirkou.modules.resteasy.rest.JSONCollectionProvider;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.ioc.services.TypeCoercer;

@ImportModule(JettyModule.class)
public class RestEasyModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(RestEasyResourceLocator.class, RestEasyResourceLocatorImpl.class);
        binder.bind(RestResourceLinkSource.class, RestResourceLinkSourceImpl.class);
    }

    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(RestEasySymbolConstants.MAPPING_PREFIX, "/api");
    }


    @Contribute(JettyManager.class)
    public static void contributeJettyManager(OrderedConfiguration<JettyHandlerCustomizer> configuration,
                                              @Autobuild JettyRestEasyCustomizer customizer) {
        configuration.add("resteasy", customizer, "after:*");
    }

    public static Gson buildGson() {
        return new GsonBuilder().serializeNulls().create();
    }

    @Contribute(RestEasyResourceLocator.class)
    public static void contributeRestEasyResourceLocator(@Symbol(CommonSymbolConstants.PRODUCTION_MODE) boolean productionMode,
                                                         TypeCoercer typeCoercer,
                                                         Gson gson,
                                                         OrderedConfiguration<Object> configuration) {
        configuration.add("gson", new GsonMessageBodyHandler(gson));
        configuration.add("json", new JSONCollectionProvider(typeCoercer, productionMode));
    }
}
