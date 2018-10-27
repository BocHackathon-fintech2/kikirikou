package io.kikirikou.modules.common.coercers;

import org.apache.tapestry5.ioc.services.Coercion;

import java.util.TimeZone;

public class TimeZoneToStringCoercer implements Coercion<TimeZone, String> {
    @Override
    public String coerce(TimeZone input) {
        return input.getID();
    }
}
