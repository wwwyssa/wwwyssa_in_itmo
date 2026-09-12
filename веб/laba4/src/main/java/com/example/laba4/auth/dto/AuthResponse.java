package com.example.laba4.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    private boolean success;
    private String username;
    private String message;
    private String token;

    public AuthResponse() {}

    public AuthResponse(boolean success, String username, String message) {
        this.success = success;
        this.username = username;
        this.message = message;
    }

    public AuthResponse(boolean success, String username, String message, String token) {
        this.success = success;
        this.username = username;
        this.message = message;
        this.token = token;
    }

}
