package io.kikirikou.modules.resteasy.managers.impl;

import io.kikirikou.modules.common.utils.AssertUtils;
import io.kikirikou.modules.jetty.managers.decl.BaseURLSource;
import io.kikirikou.modules.resteasy.managers.decl.RestResourceLinkSource;
import io.kikirikou.modules.resteasy.other.RestEasySymbolConstants;
import io.kikirikou.modules.resteasy.other.RestResourceLinkBuilder;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.TypeCoercer;

public class RestResourceLinkSourceImpl implements RestResourceLinkSource {
    private final BaseURLSource baseURLSource;
    private final TypeCoercer typeCoercer;
    private final String mappingPrefix;

    public RestResourceLinkSourceImpl(BaseURLSource baseURLSource,
                                      TypeCoercer typeCoercer,
                                      @Symbol(RestEasySymbolConstants.MAPPING_PREFIX) String mappingPrefix) {
        this.baseURLSource = baseURLSource;
        this.typeCoercer = typeCoercer;
        this.mappingPrefix = mappingPrefix;
    }

    @Override
    public RestResourceLinkBuilder build(Class resource) {
        AssertUtils.notNull(resource, "resource");

        return new RestResourceLinkBuilder(baseURLSource, mappingPrefix, typeCoercer, resource);
    }
}
