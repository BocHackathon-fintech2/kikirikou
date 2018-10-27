package io.kikirikou.modules.jetty.managers.impl;

import io.kikirikou.modules.common.utils.AssertUtils;
import io.kikirikou.modules.jetty.managers.decl.AssetLinkSource;
import io.kikirikou.modules.jetty.managers.decl.BaseURLSource;
import io.kikirikou.modules.jetty.managers.decl.ResourceSource;
import io.kikirikou.modules.jetty.other.AssetLinkBuilder;
import io.kikirikou.modules.jetty.other.JettySymbolConstants;
import org.apache.tapestry5.ioc.annotations.Symbol;

public class AssetLinkSourceImpl implements AssetLinkSource {
    private final BaseURLSource baseURLSource;
    private final ResourceSource resourceSource;
    private final String mappingPrefix;

    public AssetLinkSourceImpl(BaseURLSource baseURLSource,
                               ResourceSource resourceSource,
                               @Symbol(JettySymbolConstants.ASSETS_PREFIX) String mappingPrefix) {
        this.baseURLSource = baseURLSource;
        this.resourceSource = resourceSource;
        this.mappingPrefix = mappingPrefix;
    }

    @Override
    public AssetLinkBuilder build(String resource) {
        AssertUtils.notNull(resource, "resource");
        return new AssetLinkBuilder(baseURLSource, resourceSource.getResources(), mappingPrefix, resource);
    }
}
