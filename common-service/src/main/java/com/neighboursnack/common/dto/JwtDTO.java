package com.neighboursnack.common.dto;

public class JwtDTO {

    public record JwtResponse(String type, String token) {
        public JwtResponse(String token) {
            this("Bearer", token); // type is always "Bearer"
        }
    }

}
