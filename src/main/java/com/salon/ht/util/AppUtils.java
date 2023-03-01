package com.salon.ht.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("Cannot instantiate a App Util class");
    }

    public static String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }

    public static String hash(String input) {
        String salt = "+WW=2c*8eW#da*5#&8M#";
        input = input + salt;
        return DigestUtils.sha256Hex(input);
    }

}
