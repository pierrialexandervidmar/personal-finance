package com.system.personalfinance.dtos.category;

import java.time.Instant;
import com.system.personalfinance.enums.TransactionTypeEnum;

public record CategoryResponse(
        Long id,
        String name,
        TransactionTypeEnum type,
        Instant createdAt) {
}
