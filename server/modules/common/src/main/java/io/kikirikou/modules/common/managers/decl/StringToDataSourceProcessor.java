package io.kikirikou.modules.common.managers.decl;

import io.kikirikou.modules.common.other.DataSource;

public interface StringToDataSourceProcessor {
    DataSource process(String scheme, String value);
}
