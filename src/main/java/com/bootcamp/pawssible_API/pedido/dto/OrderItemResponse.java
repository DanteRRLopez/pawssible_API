package com.bootcamp.pawssible_API.pedido.dto;

import java.math.BigDecimal;

public record OrderItemResponse(Long id, Long productId, String productName, BigDecimal unitPrice, Integer quantity
) {
}
