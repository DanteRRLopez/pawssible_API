package com.bootcamp.pawssible_API.producto.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

    @Converter(autoApply = true)
    public class ProductCategoryConverter implements AttributeConverter<ProductCategory, Integer> {
        @Override
        public Integer convertToDatabaseColumn(ProductCategory attr) {
            return attr == null ? null : attr.getCode();
        }

        @Override
        public ProductCategory convertToEntityAttribute(Integer db) {
            return db == null ? null : ProductCategory.from(db);
        }
    }
