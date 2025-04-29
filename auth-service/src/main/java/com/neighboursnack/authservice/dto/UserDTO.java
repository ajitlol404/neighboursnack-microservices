package com.neighboursnack.authservice.dto;

import com.neighboursnack.authservice.entity.User;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class UserDTO {

    public record UserResponseDTO(
            UUID uuid,
            String name,
            String email,
            String phoneNumber,
            boolean enabled,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            UUID secretKey,
            boolean secretKeyStatus,
            List<User.Address> addresses
    ) {
        public static UserResponseDTO fromEntity(User user) {
            return new UserResponseDTO(
                    user.getUuid(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.isEnabled(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    user.getUserData() != null ? user.getUserData().getSecretKey() : null,
                    user.getUserData() != null && user.getUserData().isSecretKeyStatus(),
                    user.getAddresses() != null ? user.getAddresses() : List.of()
            );
        }
    }

}
