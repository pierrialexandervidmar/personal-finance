package com.system.personalfinance.dtos.category;

import com.system.personalfinance.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
        @NotBlank @Size(max = 80) String name,
        @NotNull TransactionType type) {
}
