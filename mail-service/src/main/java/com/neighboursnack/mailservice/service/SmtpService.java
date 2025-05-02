package com.neighboursnack.mailservice.service;


import com.neighboursnack.common.dto.EmailRequestDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpRequestDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpResponseDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpToggleRequestDTO;
import com.neighboursnack.mailservice.entity.Smtp;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing SMTP configurations.
 */
public interface SmtpService {

    /**
     * Saves a new SMTP configuration.
     *
     * @param smtpRequestDTO The DTO containing SMTP configuration details.
     * @return The saved SMTP configuration response.
     */
    SmtpResponseDTO saveSmtp(SmtpRequestDTO smtpRequestDTO);

    /**
     * Retrieves all SMTP configurations.
     *
     * @return List of all SMTP configurations.
     */
    List<SmtpResponseDTO> getAllSmtpConfigs();

    /**
     * Retrieves an SMTP configuration by UUID.
     *
     * @param uuid The UUID of the SMTP configuration.
     * @return The SMTP configuration response.
     */
    SmtpResponseDTO getSmtpByUuid(UUID uuid);

    /**
     * Updates an existing SMTP configuration.
     *
     * @param uuid           The UUID of the SMTP configuration to update.
     * @param smtpRequestDTO The DTO containing updated SMTP configuration details.
     * @return The updated SMTP configuration response.
     */
    SmtpResponseDTO updateSmtp(UUID uuid, SmtpRequestDTO smtpRequestDTO);

    /**
     * Toggles the active status of an SMTP configuration.
     *
     * @param uuid             The UUID of the SMTP configuration.
     * @param toggleRequestDTO The DTO containing the status (active/inactive).
     * @return The updated SMTP configuration response.
     */
    SmtpResponseDTO toggleSmtpStatus(UUID uuid, SmtpToggleRequestDTO toggleRequestDTO);

    /**
     * Deletes an SMTP configuration by UUID.
     *
     * @param uuid The UUID of the SMTP configuration to delete.
     */
    void deleteSmtp(UUID uuid);

    /**
     * Retrieves the active SMTP configuration.
     *
     * @return The active SMTP configuration response.
     */
    SmtpResponseDTO getActiveSmtp();

    Smtp getActiveSmtpEntity();

    void sendEmail(EmailRequestDTO emailRequestDTO);

}
