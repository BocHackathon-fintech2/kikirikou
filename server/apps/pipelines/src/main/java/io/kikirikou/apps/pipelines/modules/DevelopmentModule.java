package io.kikirikou.apps.pipelines.modules;


import io.kikirikou.modules.http.modules.HttpModule;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

@ImportModule({HttpModule.class})
public class DevelopmentModule {
    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
    }
}
