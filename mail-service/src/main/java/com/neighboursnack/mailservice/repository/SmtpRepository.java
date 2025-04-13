package com.neighboursnack.mailservice.repository;

import com.neighboursnack.mailservice.entity.Smtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing SMTP configurations.
 */
public interface SmtpRepository extends JpaRepository<Smtp, UUID> {

    /**
     * Checks if an SMTP configuration with the given name exists.
     *
     * @param name SMTP name.
     * @return true if exists, false otherwise.
     */
    boolean existsByName(String name);

    /**
     * Finds an SMTP configuration by UUID.
     *
     * @param uuid SMTP UUID.
     * @return Optional of SMTP configuration.
     */
    Optional<Smtp> findByUuid(UUID uuid);

    /**
     * Gets SMTP by UUID or throws exception if not found.
     *
     * @param uuid SMTP UUID.
     * @return SMTP configuration.
     * @throws NoSuchElementException If not found.
     */
    default Smtp findSmtpByUuid(UUID uuid) {
        return findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Smtp with [UUID: " + uuid + "] not found"));
    }

    /**
     * Deactivates all active SMTP configurations.
     */
    @Modifying
    @Query("UPDATE Smtp s SET s.isActive = false WHERE s.isActive = true")
    void deactivateAll();

    /**
     * Counts the active SMTP configurations.
     *
     * @return Number of active SMTP configurations.
     */
    long countByIsActiveTrue();

    /**
     * Finds the active SMTP configuration.
     *
     * @return Optional of active SMTP.
     */
    Optional<Smtp> findByIsActiveTrue();

    /**
     * Gets the active SMTP or throws exception if not found.
     *
     * @return Active SMTP configuration.
     * @throws NoSuchElementException If no active configuration.
     */
    default Smtp findActiveSmtp() {
        return findByIsActiveTrue()
                .orElseThrow(() -> new NoSuchElementException("No active SMTP configuration found"));
    }

}
