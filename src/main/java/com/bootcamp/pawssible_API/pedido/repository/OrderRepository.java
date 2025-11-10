package com.bootcamp.pawssible_API.pedido.repository;

import com.bootcamp.pawssible_API.pedido.model.OrderStatus;
import com.bootcamp.pawssible_API.pedido.model.Order;
import com.bootcamp.pawssible_API.usuario.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndStatus(User user, OrderStatus status); // carrito actual
    List<Order> findByUser(User user);
}
