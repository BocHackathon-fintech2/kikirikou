package io.kikirikou.modules.persistence.managers.decl;


import io.kikirikou.modules.persistence.other.CustomDSLContext;

import java.util.function.Function;

public interface QueryFactoryProvider {
    <R> R build(Function<CustomDSLContext, R> function);
}