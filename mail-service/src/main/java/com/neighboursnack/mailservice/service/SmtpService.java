package com.neighboursnack.mailservice.service;


import com.neighboursnack.mailservice.dto.SmtpDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpRequestDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SmtpService {
    SmtpResponseDTO saveSmtp(SmtpRequestDTO smtpRequestDTO);

    List<SmtpResponseDTO> getAllSmtpConfigs();

    SmtpResponseDTO getSmtpByUuid(UUID uuid);

    SmtpResponseDTO updateSmtp(UUID uuid, SmtpRequestDTO smtpRequestDTO);

}
