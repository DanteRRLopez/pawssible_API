package com.bootcamp.pawssible_API.pedido.service;

import com.bootcamp.pawssible_API.pedido.dto.*;
import com.bootcamp.pawssible_API.pedido.model.OrderStatus;
import com.bootcamp.pawssible_API.pedido.model.Order;
import com.bootcamp.pawssible_API.pedido.model.OrderItem;
import com.bootcamp.pawssible_API.pedido.repository.OrderItemRepository;
import com.bootcamp.pawssible_API.pedido.repository.OrderRepository;
import com.bootcamp.pawssible_API.producto.model.Product;
import com.bootcamp.pawssible_API.producto.repository.ProductRepository;
import com.bootcamp.pawssible_API.usuario.model.User;
import com.bootcamp.pawssible_API.usuario.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    /* === Carrito (DRAFT) === */

    @Transactional
    public OrderResponse createOrGetCart(CartCreateOrGetRequest req) {
        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no existe"));

        Order order = orderRepo.findByUserAndStatus(user, OrderStatus.DRAFT)
                .orElseGet(() -> orderRepo.save(Order.builder().user(user).status(OrderStatus.DRAFT).build()));

        recalcTotal(order);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse addItem(Long orderId, ItemAddRequest req) {
        Order order = findOrder(orderId);
        ensureDraft(order);

        Product p = productRepo.findById(req.productId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));

        // (Estrategia: no descontar stock todavía; se descuenta al confirmar la compra)
        // Si prefieres reservar stock aquí, valida req.quantity() <= p.getStock()

        // Si el item ya existe, sumamos cantidad
        OrderItem item = order.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(p.getId()))
                .findFirst()
                .orElseGet(() -> {
                    OrderItem it = OrderItem.builder()
                            .order(order)
                            .product(p)
                            .unitPrice(p.getPrice())         // capturamos precio actual
                            .quantity(0)
                            .build();
                    order.addItem(it);
                    return it;
                });

        item.setQuantity(item.getQuantity() + req.quantity());
        recalcTotal(order);
        return toResponse(orderRepo.save(order));
    }

    @Transactional
    public OrderResponse updateItemQty(Long orderId, Long itemId, ItemUpdateQtyRequest req) {
        Order order = findOrder(orderId);
        ensureDraft(order);

        OrderItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item no existe"));

        if (!item.getOrder().getId().equals(orderId))
            throw new IllegalArgumentException("El item no pertenece a la orden");

        item.setQuantity(req.quantity());
        recalcTotal(order);
        return toResponse(orderRepo.save(order));
    }

    @Transactional
    public OrderResponse removeItem(Long orderId, Long itemId) {
        Order order = findOrder(orderId);
        ensureDraft(order);

        OrderItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item no existe"));

        if (!item.getOrder().getId().equals(orderId))
            throw new IllegalArgumentException("El item no pertenece a la orden");

        order.getItems().remove(item);   // orphanRemoval = true
        recalcTotal(order);
        return toResponse(orderRepo.save(order));
    }

    /* === Confirmar / Cancelar === */

    @Transactional
    public OrderResponse confirm(Long orderId) {
        Order order = findOrder(orderId);
        ensureDraft(order);

        if (order.getItems().isEmpty()) throw new IllegalStateException("El carrito está vacío");

        // Validar y descontar stock
        order.getItems().forEach(i -> {
            Product p = i.getProduct();
            if (p.getStock() < i.getQuantity())
                throw new IllegalStateException("Sin stock para: " + p.getName());
            p.setStock(p.getStock() - i.getQuantity());
            productRepo.save(p);
        });

        recalcTotal(order);
        order.setStatus(OrderStatus.PAID); // Para un MVP asumimos pago exitoso
        return toResponse(orderRepo.save(order));
    }

    @Transactional
    public OrderResponse cancel(Long orderId) {
        Order order = findOrder(orderId);
        if (order.getStatus() == OrderStatus.PAID) {
            // si cancela una pagada, deberías devolver stock (política de negocio)
            order.getItems().forEach(i -> {
                Product p = i.getProduct();
                p.setStock(p.getStock() + i.getQuantity());
                productRepo.save(p);
            });
        }
        order.setStatus(OrderStatus.CANCELLED);
        return toResponse(orderRepo.save(order));
    }

    /* === Consultas === */

    @Transactional(readOnly = true)
    public List<OrderResponse> listByUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no existe"));
        return orderRepo.findByUser(user).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        return toResponse(findOrder(id));
    }

    /* === Helpers === */

    private Order findOrder(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Orden no existe"));
    }

    private void ensureDraft(Order order) {
        if (order.getStatus() != OrderStatus.DRAFT)
            throw new IllegalStateException("La orden no está en estado DRAFT");
    }

    private void recalcTotal(Order order) {
        BigDecimal total = order.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
    }

    private OrderResponse toResponse(Order o) {
        var items = o.getItems().stream().map(i ->
                new OrderItemResponse(i.getId(), i.getProduct().getId(),
                        i.getProduct().getName(), i.getUnitPrice(), i.getQuantity())
        ).toList();
        return new OrderResponse(o.getId(), o.getUser().getId(), o.getStatus(), o.getTotal(), items);
    }
}
