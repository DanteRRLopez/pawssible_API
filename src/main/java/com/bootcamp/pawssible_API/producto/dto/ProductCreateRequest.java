package com.bootcamp.pawssible_API.producto.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(@NotBlank @Size(max = 120) String name,
                                   @NotBlank @Size(max = 160) String slug,
                                   @Size(max = 10_000) String description,
                                   @NotNull @DecimalMin("0.0") BigDecimal price,
                                   @NotNull @Min(0) Integer stock,
                                   @Size(max = 64) String sku,
                                   @Size(max = 20) String size,
                                   @NotNull @Min(1) @Max(2) Integer categoryCode)  // 1=PRODUCT, 2=SERVICE
{}
