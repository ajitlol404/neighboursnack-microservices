package com.neighboursnack.mailservice.repository;

import com.neighboursnack.mailservice.entity.Smtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface SmtpRepository extends JpaRepository<Smtp, UUID> {

    boolean existsByName(String name);

    Optional<Smtp> findByUuid(UUID uuid);

    default Smtp findSmtpByUuid(UUID uuid) {
        return findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Smtp with [UUID: " + uuid + "] not found"));
    }

    @Modifying
    @Query("UPDATE Smtp s SET s.isActive = false WHERE s.isActive = true")
    void deactivateAll();

}
