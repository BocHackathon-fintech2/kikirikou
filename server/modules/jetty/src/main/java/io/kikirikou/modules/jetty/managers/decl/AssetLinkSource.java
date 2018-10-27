package io.kikirikou.modules.jetty.managers.decl;


import io.kikirikou.modules.jetty.other.AssetLinkBuilder;

public interface AssetLinkSource {
    AssetLinkBuilder build(String resource);
}
