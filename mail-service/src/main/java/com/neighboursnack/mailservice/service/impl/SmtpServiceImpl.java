package com.neighboursnack.mailservice.service.impl;

import com.neighboursnack.common.exception.SmtpException;
import com.neighboursnack.common.util.AppUtil;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpRequestDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpResponseDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpToggleRequestDTO;
import com.neighboursnack.mailservice.entity.Smtp;
import com.neighboursnack.mailservice.repository.SmtpRepository;
import com.neighboursnack.mailservice.service.SmtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static com.neighboursnack.common.util.AppConstant.*;
import static java.lang.Boolean.TRUE;

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
        if (TRUE.equals(smtpRequestDTO.isActive())) {
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
        if (TRUE.equals(smtpRequestDTO.isActive())) {
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

    @Override
    public Smtp getActiveSmtpEntity() {
        Smtp activeSmtp = smtpRepository.findActiveSmtp();
        activeSmtp.setPassword(AppUtil.decodeBase64(activeSmtp.getPassword()));
        return activeSmtp;
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        Smtp smtpConfig = getActiveSmtpEntity();

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpConfig.getHost());
        mailSender.setPort(smtpConfig.getPort());
        mailSender.setUsername(smtpConfig.getUsername());
        mailSender.setPassword(smtpConfig.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, TRUE.toString());
        properties.put(MAIL_SMTP_STARTTLS, Boolean.toString(smtpConfig.isSsl()));

        properties.put(MAIL_SMTP_CONNECTIONTIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));
        properties.put(MAIL_SMTP_TIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));
        properties.put(MAIL_SMTP_WRITETIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, TRUE, "UTF-8");

            helper.setFrom(smtpConfig.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new SmtpException("Failed to send email", e);
        }
    }

    private void testSmtpConfiguration(SmtpRequestDTO request) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(request.host());
        mailSender.setPort(request.port());
        mailSender.setUsername(request.username());
        mailSender.setPassword(request.password());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, TRUE.toString());
        properties.put(MAIL_SMTP_STARTTLS, Boolean.toString(request.isSsl()));

        properties.put(MAIL_SMTP_CONNECTIONTIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));
        properties.put(MAIL_SMTP_TIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));
        properties.put(MAIL_SMTP_WRITETIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));

        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            throw new SmtpException("SMTP connection failed: " + e.getMessage(), e);
        }
    }
}
