package io.kikirikou.apps.backoffice.modules;


import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import io.kikirikou.apps.backoffice.other.ApplicationSymbolConstants;
import io.kikirikou.modules.boc.modules.BocModule;
import io.kikirikou.modules.boc.other.BocSymbolConstants;
import io.kikirikou.modules.jetty.other.JettySymbolConstants;
import io.kikirikou.modules.resteasy.modules.RestEasyModule;

@ImportModule({BocModule.class, RestEasyModule.class})
public class DevelopmentModule {
    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
    	configuration.add(ApplicationSymbolConstants.DB_PATH, System.getProperty("java.io.tmpdir") + "/kikirikou.db");
    	configuration.add(ApplicationSymbolConstants.PIPELINES_URL, "http://127.0.0.1:8080/api/pipeline");
    	configuration.add(JettySymbolConstants.BIND_ADDRESS, "0.0.0.0");
    	
    	configuration.add(BocSymbolConstants.CLIENT_SECRET,"L1bI3xM0gJ3tU5lR3fM6lI1uS4aM1uV8mN1fT2sD1pS5uE5uB2");
    	configuration.add(BocSymbolConstants.CLIENT_ID, "c60fe3ab-72f8-4c3c-907e-a016f2537f5d");
    }
}
