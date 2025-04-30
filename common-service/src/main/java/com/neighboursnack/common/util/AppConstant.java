package com.neighboursnack.common.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AppConstant {

    // Private constructor to prevent instantiation
    private AppConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String BRAND_NAME = "Neighbour Snack";
    public static final String SUPPORT_TEAM = "Neighbour Snack Team";

    public static final long MAX_IMAGE_SIZE = 2L * 1024 * 1024; // 2 MB
    public static final Path IMAGE_DIRECTORY = Paths.get("products/");
    public static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png");
    public static final int VERIFICATION_EXPIRATION_MINUTES = 15;

    public static final int MAX_SMTP_CONFIGURATIONS = 5;
    public static final int SMTP_TIMEOUT_MS = 10000;
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_STARTTLS = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
    public static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    public static final String MAIL_SMTP_WRITETIMEOUT = "mail.smtp.writetimeout";

    public static final String BASE_API_PATH = "/api/v1";
    public static final String ADMIN_BASE_API_PATH = BASE_API_PATH + "/admin";

    public static final String NAME_REGEX = "^(?! )[a-zA-Z0-9 ]*(?<! )$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[^\s]*$";
}
