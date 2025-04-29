package com.neighboursnack.authservice.service;

import com.neighboursnack.authservice.dto.UnVerifiedUserDTO.UnVerifiedUserPasswordDTO;
import com.neighboursnack.authservice.dto.UnVerifiedUserDTO.UnVerifiedUserPublicResponseDTO;
import com.neighboursnack.authservice.dto.UnVerifiedUserDTO.UnVerifiedUserRequestDTO;
import com.neighboursnack.authservice.dto.UnVerifiedUserDTO.UnVerifiedUserResponseDTO;

import java.util.UUID;

public interface UnVerifiedUserService {

    UnVerifiedUserPublicResponseDTO registerUnVerifiedUser(UnVerifiedUserRequestDTO unVerifiedUserRequestDTO);

    UnVerifiedUserResponseDTO verifyLink(UUID uuid);

    void verifyAndSaveUser(UUID uuid, UnVerifiedUserPasswordDTO unVerifiedUserPasswordDTO);

}