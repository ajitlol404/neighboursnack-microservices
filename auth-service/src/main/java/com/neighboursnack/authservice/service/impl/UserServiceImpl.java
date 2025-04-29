package com.neighboursnack.authservice.service.impl;

import com.neighboursnack.authservice.dto.UserDTO;
import com.neighboursnack.authservice.entity.User;
import com.neighboursnack.authservice.repository.UserRepository;
import com.neighboursnack.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.neighboursnack.authservice.entity.Role.ROLE_ADMIN;
import static com.neighboursnack.authservice.entity.Role.ROLE_CUSTOMER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean areThereAdminUsers() {
        return userRepository.existsByRole(ROLE_ADMIN);
    }

    @Override
    @Transactional
    public void createAdminUser() {
        if (!areThereAdminUsers()) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@neighboursnack.com")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .enabled(true)
                    .role(ROLE_ADMIN)
                    .image(null)
                    .phoneNumber(null)
                    .userData(
                            User.UserData.builder()
                                    .secretKey(UUID.randomUUID())
                                    .secretKeyStatus(false)
                                    .build()
                    )
                    .addresses(List.of())
                    .build();
            userRepository.save(admin);
        }
    }

    @Override
    @Transactional
    public UserDTO.UserResponseDTO createUser(String name, String email, String password) {
        User customer = User.builder()
                .name(name.toLowerCase())
                .email(email.toLowerCase())
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .role(ROLE_CUSTOMER)
                .image(null)
                .phoneNumber(null)
                .userData(
                        User.UserData.builder()
                                .secretKey(UUID.randomUUID())
                                .secretKeyStatus(false)
                                .build()
                )
                .addresses(List.of())
                .build();
        return UserDTO.UserResponseDTO.fromEntity(userRepository.save(customer));
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
