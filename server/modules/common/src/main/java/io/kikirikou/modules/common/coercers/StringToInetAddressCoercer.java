package io.kikirikou.modules.common.coercers;

import io.kikirikou.modules.common.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.services.Coercion;

import java.net.InetAddress;

public class StringToInetAddressCoercer implements Coercion<String, InetAddress> {
    @Override
    public InetAddress coerce(String input) {
        if (StringUtils.isBlank(input))
            return null;

        return IpUtils.toAddress(input);
    }
}
