package com.system.personalfinance.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank
    @Email
    @Size(max = 180)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    public LoginRequest() {}

    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

}
