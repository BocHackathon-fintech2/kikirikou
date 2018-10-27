package io.kikirikou.modules.bootstrap.modules;

import io.kikirikou.modules.bootstrap.managers.decl.SignalManager;
import io.kikirikou.modules.bootstrap.managers.impl.SignalManagerImpl;
import io.kikirikou.modules.bootstrap.other.constants.BootstrapSymbolConstants;
import io.kikirikou.modules.bootstrap.utils.VersionUtils;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;


public class BootstrapModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(SignalManager.class, SignalManagerImpl.class).eagerLoad();
    }

    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(BootstrapSymbolConstants.APPLICATION_VERSION, VersionUtils.readVersionNumber("META-INF/version.properties"));
    }
}
