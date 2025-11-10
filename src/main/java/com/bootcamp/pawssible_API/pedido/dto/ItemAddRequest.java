package com.bootcamp.pawssible_API.pedido.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemAddRequest(@NotNull Long productId, @NotNull @Min(1) Integer quantity) {
}
