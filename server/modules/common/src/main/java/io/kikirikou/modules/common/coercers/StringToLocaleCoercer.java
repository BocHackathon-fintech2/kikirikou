package io.kikirikou.modules.common.coercers;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.services.Coercion;

import java.util.Locale;

public class StringToLocaleCoercer implements Coercion<String, Locale> {
    @Override
    public Locale coerce(String input) {
        if (StringUtils.isBlank(input))
            return null;

        try {
            return LocaleUtils.toLocale(input);
        } catch (Throwable ex) {
            return null;
        }
    }
}
