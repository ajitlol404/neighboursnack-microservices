package com.neighboursnack.authservice.repository;

import com.neighboursnack.authservice.entity.Role;
import com.neighboursnack.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByRole(Role role);

    boolean existsByEmail(String email);

    boolean existsByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    default User findUserByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found."));
    }

    default User findUserByUuid(UUID uuid) {
        return findById(uuid).orElseThrow(() -> new NoSuchElementException("User not found."));
    }
}
