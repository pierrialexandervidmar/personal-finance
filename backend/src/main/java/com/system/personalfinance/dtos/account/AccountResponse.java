package com.system.personalfinance.dtos.account;

import java.time.Instant;

import com.system.personalfinance.enums.AccountType;

public class AccountResponse {

    private Long id;
    private String name;
    private AccountType type;
    private Long initialBalanceCents;
    private Instant createdAt;

    public AccountResponse(Long id, String name, AccountType type, Long initialBalanceCents, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.initialBalanceCents = initialBalanceCents;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }
}
