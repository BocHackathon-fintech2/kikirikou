package io.kikirikou.modules.resteasy.managers.impl;

import io.kikirikou.modules.resteasy.managers.decl.RestEasyResourceLocator;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.jboss.resteasy.util.GetRestful;

import javax.ws.rs.ext.Provider;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RestEasyResourceLocatorImpl implements RestEasyResourceLocator {
    private final List<Object> resources;

    @SuppressWarnings("unchecked")
    public RestEasyResourceLocatorImpl(ObjectLocator locator,
                                       ClassNameLocator classNameLocator,
                                       List<Object> configuration) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        this.resources = configuration.stream().flatMap((Function<Object, Stream<Object>>) s -> {
            if (s instanceof String)
                return classNameLocator.locateClassNames((String) s).stream().map(className -> {
                    try {
                        Class clazz = contextClassLoader.loadClass(className);
                        Class rootResourceClass = GetRestful.getRootResourceClass(clazz);

                        if (rootResourceClass != null)
                            if (rootResourceClass.equals(clazz))
                                if (!clazz.isInterface())
                                    return locator.autobuild(clazz);
                                else
                                    try {
                                        return locator.getService(rootResourceClass);
                                    } catch (RuntimeException e) {
                                        return locator.proxy(rootResourceClass, clazz);
                                    }
                            else if (clazz.isAnnotationPresent(Provider.class))
                                return locator.autobuild(clazz);
                            else
                                return null;
                    } catch (ClassNotFoundException ignored) {
                    }

                    return null;
                });
            else
                return Stream.of(s);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Collection<Object> getResources() {
        return resources;
    }
}
