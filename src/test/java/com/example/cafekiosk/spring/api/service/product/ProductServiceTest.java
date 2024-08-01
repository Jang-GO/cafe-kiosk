package com.example.cafekiosk.spring.api.service.product;

import com.example.cafekiosk.spring.api.service.product.request.ProductCreateRequest;
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingType.*;
import static com.example.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호+1 이다")
    @Test
    void createProduct() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("아메리카노")
                .price(4000).build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingType(HOLD)
                .name("카페라떼")
                .price(4500).build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingType(STOP_SELLING)
                .name("팥빙수")
                .price(7000).build();
        productRepository.saveAll(List.of(product1,product2,product3));

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when

        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingType","name", "price")
                .contains("004", HANDMADE, SELLING, "카푸치노", 5000);

        List<Product> all = productRepository.findAll();
        assertThat(all).hasSize(4)
                .extracting("productNumber", "type", "sellingType","name", "price")
                .contains(
                        tuple("004", HANDMADE, SELLING, "카푸치노", 5000)
                );
    }

    @DisplayName("상품이 하나도 없는경우 신규 상품을 등록하면 상품번호는 001이다")
    @Test
    void createProductWhenProductsIsEmpty() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when

        ProductResponse productResponse = productService.createProduct(request);
        List<Product> all = productRepository.findAll();

        // then
        assertThat(all).hasSize(1)
                .extracting("productNumber", "type", "sellingType","name", "price")
                .contains(
                        tuple("001", HANDMADE, SELLING, "카푸치노", 5000)
                );
    }
}