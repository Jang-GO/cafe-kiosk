package com.example.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingType.*;
import static com.example.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest // 스프링부트 서버를 띄워서 통합테스트
@DataJpaTest // 얘도 마찬가지 but SpringBootTest 보다 가벼움(Jpa 관련 빈들만 주입)
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
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

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        // when
        List<Product> products = productRepository.findAllBySellingTypeIn(List.of(SELLING, HOLD));
        // then

        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingType")
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",SELLING),
                        tuple("002","카페라떼",HOLD)
                );
    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다")
    @Test
    void findAllByProductNumbersIn() {
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

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));
        // then

        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingType")
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",SELLING),
                        tuple("002","카페라떼",HOLD)

                );
    }

    @DisplayName("가장 마지막으로 저장된 상품의 상품번호를 가져온다.")
    @Test
    void findLatestProductNumber() {
        // given
        String targetProductNumber = "003";


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
                .productNumber(targetProductNumber)
                .type(HANDMADE)
                .sellingType(STOP_SELLING)
                .name("팥빙수")
                .price(7000).build();

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        // when
        String productNumber = productRepository.findLatestProductNumber();
        // then

        assertThat(productNumber).isEqualTo("003");
    }

    @DisplayName("가장 마지막으로 저장된 상품의 상품번호를 가져올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductsIsEmpty() {
        // when
        String productNumber = productRepository.findLatestProductNumber();
        // then
        assertThat(productNumber).isNull();
    }
}