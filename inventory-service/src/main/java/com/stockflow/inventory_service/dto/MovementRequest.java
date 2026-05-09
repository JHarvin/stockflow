package com.stockflow.inventory_service.dto;

import jakarta.validation.constraints.*;

public record MovementRequest(
        @NotNull Long productId,
        @NotBlank String type,
        @Min(1) Integer quantity,
        String reason
) {}
