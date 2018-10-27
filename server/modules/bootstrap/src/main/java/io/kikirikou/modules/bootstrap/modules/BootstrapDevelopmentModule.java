package io.kikirikou.modules.bootstrap.modules;


import io.kikirikou.modules.common.other.CommonSymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

public class BootstrapDevelopmentModule {
    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(CommonSymbolConstants.PRODUCTION_MODE, false);
    }
}
