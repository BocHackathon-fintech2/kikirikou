package io.kikirikou.modules.common.coercers;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.services.Coercion;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StringToPathCoercer implements Coercion<String, Path> {
    @Override
    public Path coerce(String input) {
        if (StringUtils.isBlank(input))
            return null;
        return Paths.get(input);
    }
}
