package com.system.personalfinance.dtos.transaction;

import java.time.Instant;

import com.system.personalfinance.enums.TransactionType;

public class TransactionResponse {

    private Long id;
    private Long accountId;
    private Long categoryId;
    private TransactionType type;
    private Long amountCents;
    private String description;
    private Instant occurredAt;
    private Instant createdAt;

    public TransactionResponse(
            Long id,
            Long accountId,
            Long categoryId,
            TransactionType type,
            Long amountCents,
            String description,
            Instant occurredAt,
            Instant createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.type = type;
        this.amountCents = amountCents;
        this.description = description;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public TransactionType getType() {
        return type;
    }

    public Long getAmountCents() {
        return amountCents;
    }

    public String getDescription() {
        return description;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
