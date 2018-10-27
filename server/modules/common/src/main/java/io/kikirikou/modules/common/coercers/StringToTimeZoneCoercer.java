package io.kikirikou.modules.common.coercers;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.Coercion;

import java.util.Set;
import java.util.TimeZone;

public class StringToTimeZoneCoercer implements Coercion<String, TimeZone> {
    private final Set<String> timeZones;

    public StringToTimeZoneCoercer() {
        this.timeZones = CollectionFactory.newSet(TimeZone.getAvailableIDs());
    }

    @Override
    public TimeZone coerce(String input) {
        if (StringUtils.isBlank(input) || !timeZones.contains(input))
            return null;

        return TimeZone.getTimeZone(input);
    }
}
