package io.kikirikou.modules.jetty.managers.impl;

import io.kikirikou.modules.jetty.managers.decl.ResourceSource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;

import java.util.List;

public class ResourceSourceImpl implements ResourceSource {
    private final ResourceCollection resourceCollection;

    public ResourceSourceImpl(List<Resource> resources) {
        this.resourceCollection = new ResourceCollection(resources.toArray(new Resource[0]));
    }

    @Override
    public ResourceCollection getResources() {
        return resourceCollection;
    }
}
