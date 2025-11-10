package com.bootcamp.pawssible_API.producto.dto;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String slug, String description,
                              BigDecimal price, Integer stock, String sku, String size,
                              Integer categoryCode) {
}
