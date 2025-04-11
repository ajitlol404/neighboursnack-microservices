package com.neighboursnack.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AppUtil {

    public static String encodeBase64(String plainText) {
        if (plainText == null) return null;
        return Base64.getEncoder()
                .encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(String encodedText) {
        if (encodedText == null) return null;
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

}
