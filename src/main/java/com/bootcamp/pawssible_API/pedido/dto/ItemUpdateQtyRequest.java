package com.bootcamp.pawssible_API.pedido.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemUpdateQtyRequest(@NotNull @Min(1) Integer quantity) {
}
