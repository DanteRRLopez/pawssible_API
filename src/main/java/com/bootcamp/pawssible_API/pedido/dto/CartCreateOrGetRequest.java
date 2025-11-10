package com.bootcamp.pawssible_API.pedido.dto;

import jakarta.validation.constraints.NotNull;

public record CartCreateOrGetRequest(@NotNull Long userId) {
}
