package com.neighboursnack.common.util;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
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

    public static String maskedEmail(String email) {
        // Find the index of '@' to split the email
        int atIndex = email.indexOf('@');

        // Extract the part before '@' and after '@'
        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);

        // Mask the local part (leave the first character and replace the rest with *)
        String maskedLocalPart = localPart.charAt(0) + "*".repeat(localPart.length() - 1);

        // Mask the domain part (replace everything before the '.' with *)
        String[] domainParts = domainPart.split("\\.");
        String maskedDomainPart = "*".repeat(domainParts[0].length()) + "." + domainParts[1];

        // Combine the masked parts and return
        return maskedLocalPart + "@" + maskedDomainPart;
    }

    public static String normalizeName(String name) {
        // Convert to lowercase
        String normalized = name.toLowerCase();
        // Remove accents/diacritics
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        // Replace spaces and special characters with hyphens
        normalized = normalized.replaceAll("[^a-z0-9]+", "-");
        // Remove leading/trailing hyphens
        normalized = normalized.replaceAll("^-+|-+$", "");
        return normalized;
    }

}
