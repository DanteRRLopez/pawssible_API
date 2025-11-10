package com.bootcamp.pawssible_API.pedido.controller;

import com.bootcamp.pawssible_API.pedido.dto.CartCreateOrGetRequest;
import com.bootcamp.pawssible_API.pedido.dto.ItemAddRequest;
import com.bootcamp.pawssible_API.pedido.dto.ItemUpdateQtyRequest;
import com.bootcamp.pawssible_API.pedido.dto.OrderResponse;
import com.bootcamp.pawssible_API.pedido.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/cart")
    public ResponseEntity<OrderResponse> createOrGetCart(@Valid @RequestBody CartCreateOrGetRequest req){
        OrderResponse res = service.createOrGetCart(req);
        return ResponseEntity.created(URI.create("/api/v1/orders/"+res.id())).body(res);
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItem(@PathVariable Long orderId, @Valid @RequestBody ItemAddRequest req){
        return ResponseEntity.ok(service.addItem(orderId, req));
    }

    @PutMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderResponse> updateItemQty(@PathVariable Long orderId, @PathVariable Long itemId,
                                                       @Valid @RequestBody ItemUpdateQtyRequest req){
        return ResponseEntity.ok(service.updateItemQty(orderId, itemId, req));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderResponse> removeItem(@PathVariable Long orderId, @PathVariable Long itemId){
        return ResponseEntity.ok(service.removeItem(orderId, itemId));
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirm(@PathVariable Long orderId){
        return ResponseEntity.ok(service.confirm(orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Long orderId){
        return ResponseEntity.ok(service.cancel(orderId));
    }

    /* Consultas */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long orderId){
        return ResponseEntity.ok(service.get(orderId));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<OrderResponse>> listByUser(@PathVariable Long userId){
        return ResponseEntity.ok(service.listByUser(userId));
    }


}
