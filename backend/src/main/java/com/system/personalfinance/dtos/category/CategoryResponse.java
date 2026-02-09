package com.system.personalfinance.dtos.category;

import java.time.Instant;
import com.system.personalfinance.enums.TransactionType;

public record CategoryResponse(
        Long id,
        String name,
        TransactionType type,
        Instant createdAt) {
}
