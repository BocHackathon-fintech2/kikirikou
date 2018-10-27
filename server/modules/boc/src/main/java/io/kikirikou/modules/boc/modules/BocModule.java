package io.kikirikou.modules.boc.modules;


import io.kikirikou.modules.boc.other.BocSymbolConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

@ImportModule(value = {BocModule.class})
public class BocModule {
    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(BocSymbolConstants.CLIENT_ID, StringUtils.EMPTY);
        configuration.add(BocSymbolConstants.CLIENT_SECRET,StringUtils.EMPTY);
        configuration.add(BocSymbolConstants.BASE_URL, "https://sandbox-apis.bankofcyprus.com/df-boc-org-sb/sb/psd2");
    }
}
