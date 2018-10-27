package io.kikirikou.modules.common.coercers;

import org.apache.tapestry5.ioc.services.Coercion;

import java.util.Locale;

public class LocaleToStringCoercer implements Coercion<Locale, String> {
    @Override
    public String coerce(Locale input) {
        return input.toString();
    }
}
