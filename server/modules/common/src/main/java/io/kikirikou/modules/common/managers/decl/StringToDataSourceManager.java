package io.kikirikou.modules.common.managers.decl;


import io.kikirikou.modules.common.other.DataSource;

public interface StringToDataSourceManager {
    String SEPARATOR = "/";
    String SCHEME_SEPARATOR = "://";

    DataSource get(String s);
}