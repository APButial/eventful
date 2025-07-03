package com.btp.appfx.service;
import org.apache.commons.lang3.RandomStringUtils;

public class UserKeyGenerator {
    static final int LENGTH = 16;
    public static String getKey() {
        return RandomStringUtils.randomAlphanumeric(LENGTH);
    }
}
