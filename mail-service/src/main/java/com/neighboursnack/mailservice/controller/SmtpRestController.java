package com.neighboursnack.mailservice.controller;

import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpRequestDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpResponseDTO;
import com.neighboursnack.mailservice.dto.SmtpDTO.SmtpToggleRequestDTO;
import com.neighboursnack.mailservice.service.SmtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static com.neighboursnack.common.util.AppConstant.BASE_API_PATH;

@RestController
@RequestMapping(BASE_API_PATH + "/smtp")
@RequiredArgsConstructor
public class SmtpRestController {

    private final SmtpService smtpService;

    @PostMapping
    public ResponseEntity<SmtpResponseDTO> saveSmtp(@Valid @RequestBody SmtpRequestDTO smtpRequestDTO) {
        SmtpResponseDTO smtpResponseDTO = smtpService.saveSmtp(smtpRequestDTO);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(smtpResponseDTO.uuid())
                .toUri()).body(smtpResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<SmtpResponseDTO>> getAllSmtpConfigs() {
        return ResponseEntity.ok(smtpService.getAllSmtpConfigs());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<SmtpResponseDTO> getSmtpByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(smtpService.getSmtpByUuid(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<SmtpResponseDTO> updateSmtp(
            @PathVariable UUID uuid,
            @Valid @RequestBody SmtpRequestDTO smtpRequestDTO) {
        return ResponseEntity.ok(smtpService.updateSmtp(uuid, smtpRequestDTO));
    }

    @PatchMapping("/{uuid}/toggle")
    public ResponseEntity<SmtpResponseDTO> toggleSmtpActiveStatus(
            @PathVariable UUID uuid,
            @RequestBody SmtpToggleRequestDTO toggleRequestDTO) {
        return ResponseEntity.ok(smtpService.toggleSmtpStatus(uuid, toggleRequestDTO));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteSmtp(@PathVariable UUID uuid) {
        smtpService.deleteSmtp(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<SmtpResponseDTO> getActiveSmtp() {
        return ResponseEntity.ok(smtpService.getActiveSmtp());
    }

}
