package com.bootcamp.pawssible_API.pedido.repository;

import com.bootcamp.pawssible_API.pedido.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository  extends JpaRepository<OrderItem, Long> {
}
