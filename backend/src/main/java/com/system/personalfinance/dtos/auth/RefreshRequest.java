package com.system.personalfinance.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RefreshRequest {

    @NotBlank
    @Size(min = 10, max = 500)
    private String refreshToken;

    public RefreshRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
