package io.kikirikou.apps.pipelines.modules;


import io.kikirikou.modules.boc.modules.BocModule;
import io.kikirikou.modules.boc.other.BocSymbolConstants;
import io.kikirkou.modules.resteasy.modules.RestEasyModule;
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
    	configuration.add(BocSymbolConstants.CLIENT_SECRET,"xH8lQ5tM7eF1xA0xF6yD5hP4pW7vU4lY3qL8uF6iN7fU5eI8bG");
    	configuration.add(BocSymbolConstants.CLIENT_ID, "45509445-67e9-476b-b946-f667c6d465a9");
    	
    }
}
