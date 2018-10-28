package io.kikirikou.apps.backoffice.modules;


import io.kikirikou.modules.jetty.other.JettySymbolConstants;
import io.kikirikou.modules.resteasy.managers.decl.RestEasyResourceLocator;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import io.kikirikou.apps.backoffice.managers.decl.PipelineExecutor;
import io.kikirikou.apps.backoffice.managers.decl.PipelineManager;
import io.kikirikou.apps.backoffice.managers.impl.PipelineExecutorImpl;
import io.kikirikou.apps.backoffice.managers.impl.PipelineManagerImpl;
import io.kikirikou.modules.bootstrap.other.constants.BootstrapConstants;

public class BackofficeModule {
    public static void bind(ServiceBinder binder) {
    	binder.bind(PipelineManager.class, PipelineManagerImpl.class);
    	binder.bind(PipelineExecutor.class, PipelineExecutorImpl.class).eagerLoad();
    }

    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
    }

    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(JettySymbolConstants.BIND_PORT,8081);
    }

    @Contribute(RestEasyResourceLocator.class)
    public static void contributeRestEasyResourceLocator(@Symbol(BootstrapConstants.APP_PACKAGE) String applicationPackage,
                                                         OrderedConfiguration<Object> configuration) {
        configuration.add("application", applicationPackage + ".rest");
    }
}
