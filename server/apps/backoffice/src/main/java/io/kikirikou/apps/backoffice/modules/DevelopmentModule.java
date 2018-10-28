package io.kikirikou.apps.backoffice.modules;


import io.kikirikou.modules.boc.modules.BocModule;
import io.kikirikou.modules.boc.other.BocSymbolConstants;
import io.kikirikou.modules.resteasy.modules.RestEasyModule;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

@ImportModule({BocModule.class, RestEasyModule.class})
public class DevelopmentModule {
    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
    	configuration.add(BocSymbolConstants.CLIENT_SECRET,"L1bI3xM0gJ3tU5lR3fM6lI1uS4aM1uV8mN1fT2sD1pS5uE5uB2");
    	configuration.add(BocSymbolConstants.CLIENT_ID, "c60fe3ab-72f8-4c3c-907e-a016f2537f5d");
    }
}
