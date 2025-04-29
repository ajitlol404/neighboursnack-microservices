package com.neighboursnack.authservice.service;

import com.neighboursnack.authservice.dto.UserDTO;
import com.neighboursnack.authservice.dto.UserDTO.UserResponseDTO;

public interface UserService {

    boolean areThereAdminUsers();

    void createAdminUser();

    UserResponseDTO createUser(String name, String email, String password);

    boolean userExistsByEmail(String email);

}