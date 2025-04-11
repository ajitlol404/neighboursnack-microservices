package com.neighboursnack.mailservice.dto;

import com.neighboursnack.common.util.AppUtil;
import com.neighboursnack.mailservice.entity.Smtp;
import jakarta.validation.constraints.*;

import java.util.UUID;

public class SmtpDTO {

    public record SmtpRequestDTO(
            @NotBlank(message = "SMTP name is required")
            @Size(min = 3, max = 100, message = "SMTP name must be between 3 and 100 characters")
            @Pattern(
                    regexp = "^[a-zA-Z0-9()\\-]+$",
                    message = "SMTP name can only contain letters, numbers, parentheses (), and hyphens -"
            )
            String name,

            @NotBlank(message = "SMTP host is required")
            @Size(max = 255, message = "SMTP host must not exceed 255 characters")
            String host,

            @NotNull(message = "SMTP port is required")
            @Min(value = 1, message = "Port must be greater than 0")
            @Max(value = 65535, message = "Port must be less than or equal to 65535")
            Integer port,

            @NotBlank(message = "Username is required")
            @Size(max = 255, message = "Username must not exceed 255 characters")
            String username,

            @NotBlank(message = "Password is required")
            @Size(max = 255, message = "Password must not exceed 255 characters")
            String password,

            @NotNull(message = "SSL flag is required")
            Boolean isSsl,

            @NotNull(message = "Active status is required")
            Boolean isActive
    ) {
        public Smtp toEntity() {
            return Smtp.builder()
                    .name(name)
                    .host(host)
                    .port(port)
                    .username(username)
                    .password(AppUtil.encodeBase64(password))
                    .isSsl(isSsl)
                    .isActive(isActive)
                    .build();
        }

        public Smtp updateSmtp(Smtp smtp) {
            smtp.setName(name);
            smtp.setHost(host);
            smtp.setPort(port);
            smtp.setUsername(username);
            smtp.setPassword(password);
            smtp.setSsl(isSsl);
            smtp.setActive(isActive);
            return smtp;
        }
    }

    public record SmtpResponseDTO(
            UUID uuid,
            String name,
            String host,
            Integer port,
            String username,
            Boolean isSsl,
            Boolean isActive
    ) {
        public static SmtpResponseDTO fromEntity(Smtp smtp) {
            return new SmtpResponseDTO(
                    smtp.getUuid(),
                    smtp.getName(),
                    smtp.getHost(),
                    smtp.getPort(),
                    smtp.getUsername(),
                    smtp.isSsl(),
                    smtp.isActive()
            );
        }
    }

}
