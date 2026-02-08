package com.system.personalfinance.dtos.account;

import com.system.personalfinance.enums.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateAccountRequest {

    @NotBlank
    @Size(min = 2, max = 80)
    private String name;

    @NotNull
    private AccountType type;

    private Long initialBalanceCents;

    public CreateAccountRequest() {
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public Long getInitialBalanceCents() {
        return initialBalanceCents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setInitialBalanceCents(Long initialBalanceCents) {
        this.initialBalanceCents = initialBalanceCents;
    }
}