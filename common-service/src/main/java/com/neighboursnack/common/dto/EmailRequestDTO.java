package com.neighboursnack.common.dto;

public record EmailRequestDTO(
        String toEmail,
        String subject,
        String bodyHtml
) {
}
