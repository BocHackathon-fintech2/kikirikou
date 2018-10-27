package io.kikirikou.modules.common.utils;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {
    public static long toLong(InetAddress ip) {
        AssertUtils.notNull(ip, "ip");
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }

    public static InetAddress toAddress(String input) {
        AssertUtils.hasText(input, "input");
        try {
            return InetAddress.getByName(input);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
