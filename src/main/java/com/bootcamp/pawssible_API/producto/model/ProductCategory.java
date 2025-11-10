package com.bootcamp.pawssible_API.producto.model;

public enum ProductCategory {
    PRODUCT(1), SERVICE(2);
    private final int code;

    ProductCategory(int code){
        this.code = code;
    }
    public int getCode() {
        return code;
    }
    public static ProductCategory from(int code) {
        return code == 1 ? PRODUCT : SERVICE;
    }
}
