package com.bootcamp.pawssible_API.pedido.dto;

import com.bootcamp.pawssible_API.pedido.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(Long id, Long userId, OrderStatus status, BigDecimal total, List<OrderItemResponse> items
) {
}
