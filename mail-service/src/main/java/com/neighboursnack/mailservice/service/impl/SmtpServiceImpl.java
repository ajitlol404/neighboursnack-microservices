package com.neighboursnack.mailservice.service.impl;

import com.neighboursnack.common.exception.SmtpException;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpRequestDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpResponseDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpToggleRequestDTO;
import com.neighboursnack.mailservice.entity.Smtp;
import com.neighboursnack.mailservice.repository.SmtpRepository;
import com.neighboursnack.mailservice.service.SmtpService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static com.neighboursnack.common.util.AppConstant.MAX_SMTP_CONFIGURATIONS;
import static com.neighboursnack.common.util.AppConstant.SMTP_TIMEOUT_MS;

@Service
@RequiredArgsConstructor
public class SmtpServiceImpl implements SmtpService {

    private final SmtpRepository smtpRepository;

    @Override
    @Transactional
    public SmtpResponseDTO saveSmtp(SmtpRequestDTO smtpRequestDTO) {
        // Check if the max limit of 5 configurations has been reached
        if (smtpRepository.count() >= MAX_SMTP_CONFIGURATIONS) {
            throw new IllegalStateException("Maximum of " + MAX_SMTP_CONFIGURATIONS + " SMTP configurations allowed");
        }

        // Check if the name already exists
        if (smtpRepository.existsByName(smtpRequestDTO.name())) {
            throw new IllegalArgumentException("SMTP configuration with the same name already exists");
        }

        testSmtpConfiguration(smtpRequestDTO);

        // If this config is marked as active, deactivate all existing active configs
        if (Boolean.TRUE.equals(smtpRequestDTO.isActive())) {
            smtpRepository.deactivateAll(); // custom update query we'll define below
        }

        Smtp smtp = smtpRequestDTO.toEntity();
        Smtp savedSmtp = smtpRepository.save(smtp);
        return SmtpResponseDTO.fromEntity(savedSmtp);
    }

    @Override
    public List<SmtpResponseDTO> getAllSmtpConfigs() {
        return smtpRepository.findAll()
                .stream()
                .map(SmtpResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public SmtpResponseDTO getSmtpByUuid(UUID uuid) {
        return SmtpResponseDTO.fromEntity(smtpRepository.findSmtpByUuid(uuid));
    }

    @Override
    @Transactional
    public SmtpResponseDTO updateSmtp(UUID uuid, SmtpRequestDTO smtpRequestDTO) {

        // Find existing entity
        Smtp existing = smtpRepository.findSmtpByUuid(uuid);

        // Ensure the configuration works before saving
        testSmtpConfiguration(smtpRequestDTO);


        // Deactivate others if this is set to active
        if (Boolean.TRUE.equals(smtpRequestDTO.isActive())) {
            smtpRepository.deactivateAll();
        }

        // Update entity
        Smtp updated = smtpRequestDTO.updateSmtp(existing);
        Smtp saved = smtpRepository.save(updated);

        return SmtpResponseDTO.fromEntity(saved);
    }

    @Override
    @Transactional
    public SmtpResponseDTO toggleSmtpStatus(UUID uuid, SmtpToggleRequestDTO toggleRequestDTO) {
        Smtp smtp = smtpRepository.findSmtpByUuid(uuid);
        boolean isActive = toggleRequestDTO.isActive();

        if (isActive && !smtp.isActive()) {
            smtpRepository.deactivateAll();
        }

        smtp.setActive(isActive);
        Smtp updated = smtpRepository.save(smtp);

        return SmtpResponseDTO.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteSmtp(UUID uuid) {
        Smtp smtp = smtpRepository.findSmtpByUuid(uuid);

        // Optional: Prevent deleting if it's the only active config
        if (smtp.isActive() && smtpRepository.countByIsActiveTrue() == 1) {
            throw new IllegalStateException("Cannot delete the only active SMTP configuration");
        }

        smtpRepository.delete(smtp);
    }

    @Override
    public SmtpResponseDTO getActiveSmtp() {
        return SmtpResponseDTO.fromEntity(smtpRepository.findActiveSmtp());
    }

    private void testSmtpConfiguration(SmtpRequestDTO request) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(request.host());
        mailSender.setPort(request.port());
        mailSender.setUsername(request.username());
        mailSender.setPassword(request.password());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", Boolean.TRUE.toString());
        properties.put("mail.smtp.starttls.enable", Boolean.toString(!request.isSsl()));
        properties.put("mail.smtp.ssl.enable", Boolean.toString(request.isSsl()));

        // Set timeouts (10 seconds = 10000 milliseconds)
        properties.put("mail.smtp.connectiontimeout", String.valueOf(SMTP_TIMEOUT_MS));
        properties.put("mail.smtp.timeout", String.valueOf(SMTP_TIMEOUT_MS));
        properties.put("mail.smtp.writetimeout", String.valueOf(SMTP_TIMEOUT_MS));

        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            throw new SmtpException("SMTP connection failed: " + e.getMessage(), e);
        }
    }
}
