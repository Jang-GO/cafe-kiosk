package com.example.cafekiosk.spring.api.service.product.request;

import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductSellingType;
import com.example.cafekiosk.spring.domain.product.ProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreateRequest {

    private ProductType type;
    private ProductSellingType sellingType;
    private String name;
    private int price;

    @Builder
    public ProductCreateRequest(ProductType type, ProductSellingType sellingType, String name, int price) {
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .sellingType(sellingType)
                .name(name)
                .price(price)
                .build();
    }
}
