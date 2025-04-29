package com.neighboursnack.authservice.repository;

import com.neighboursnack.authservice.entity.UnVerifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface UnVerifiedUserRepository extends JpaRepository<UnVerifiedUser, UUID> {

    boolean existsByEmail(String email);

    Optional<UnVerifiedUser> findByUuid(UUID uuid);

    default UnVerifiedUser findUnVerifiedUserByUuid(UUID uuid) {
        return findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("UnVerifiedUser with [UUID= " + uuid + "] not found"));
    }

    Optional<UnVerifiedUser> findByEmail(String email);

    default UnVerifiedUser findUnVerifiedUserByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("UnVerifiedUser with [EMAIL= " + email + "] not found"));
    }
}
