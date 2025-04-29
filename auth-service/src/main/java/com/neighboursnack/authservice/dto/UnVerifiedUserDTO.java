package com.neighboursnack.authservice.dto;

import com.neighboursnack.authservice.entity.UnVerifiedUser;
import com.neighboursnack.common.util.AppUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import static com.neighboursnack.common.util.AppConstant.PASSWORD_REGEX;

public class UnVerifiedUserDTO {

    public record UnVerifiedUserRequestDTO(
            @NotBlank(message = "User name is required")
            @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
            @Pattern(regexp = "^[A-Za-z0-9\\-'/()& ]+$", message = "Name can only contain letters, numbers, hyphens (-), apostrophes ('), slashes (/), parentheses (()), and ampersands (&).")
            String name,

            @NotBlank(message = "Email is required")
            @Email(message = "Invalid email format")
            @Size(max = 320, message = "Email must not exceed 320 characters")
            String email) {

        // Convert to entity
        public UnVerifiedUser toEntity() {
            return UnVerifiedUser.builder()
                    .name(name)
                    .email(email)
                    .build();
        }

        // Update entity with the values from the DTO (if needed)
        public UnVerifiedUser updateUnVerifiedUser(UnVerifiedUser unVerifiedUser) {
            unVerifiedUser.setName(name);
            unVerifiedUser.setEmail(email);
            return unVerifiedUser;
        }
    }

    public record UnVerifiedUserResponseDTO(UUID uuid, String name, String email) {

        // Convert entity to response DTO
        public static UnVerifiedUserResponseDTO fromEntity(UnVerifiedUser unVerifiedUser) {
            return new UnVerifiedUserResponseDTO(
                    unVerifiedUser.getUuid(),
                    unVerifiedUser.getName(),
                    AppUtil.maskedEmail(unVerifiedUser.getEmail()));
        }
    }

    public record UnVerifiedUserPublicResponseDTO(UUID uuid, String name, String email) {

        // Convert entity to public response DTO
        public static UnVerifiedUserPublicResponseDTO fromEntity(UnVerifiedUser unVerifiedUser) {
            return new UnVerifiedUserPublicResponseDTO(
                    unVerifiedUser.getUuid(),
                    unVerifiedUser.getName(),
                    AppUtil.maskedEmail(unVerifiedUser.getEmail())
            );
        }
    }

    public record UnVerifiedUserPasswordDTO(
            @NotBlank @Size(min = 8, max = 30) @Pattern(regexp = PASSWORD_REGEX)
            String password) {
    }

}