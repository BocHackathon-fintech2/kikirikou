package io.kikirikou.modules.resteasy.managers.decl;


import io.kikirikou.modules.resteasy.other.RestResourceLinkBuilder;

public interface RestResourceLinkSource {
    RestResourceLinkBuilder build(Class resource);
}
