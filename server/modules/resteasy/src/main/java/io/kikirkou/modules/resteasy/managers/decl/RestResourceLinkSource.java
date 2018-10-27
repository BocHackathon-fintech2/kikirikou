package io.kikirkou.modules.resteasy.managers.decl;


import io.kikirkou.modules.resteasy.other.RestResourceLinkBuilder;

public interface RestResourceLinkSource {
    RestResourceLinkBuilder build(Class resource);
}
