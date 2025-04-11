package com.neighboursnack.common.util;

public class AppConstant {

    // Private constructor to prevent instantiation
    private AppConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final int MAX_SMTP_CONFIGURATIONS = 5;

    // Unified SMTP Timeout (in milliseconds)
    public static final int SMTP_TIMEOUT_MS = 10000;
    public static final String BASE_API_PATH = "/api/v1";
}
