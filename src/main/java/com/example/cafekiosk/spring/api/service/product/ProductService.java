package com.example.cafekiosk.spring.api.service.product;

import com.example.cafekiosk.spring.api.service.product.request.ProductCreateRequest;
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductSellingType;
import com.example.cafekiosk.spring.domain.product.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.cafekiosk.spring.domain.product.ProductSellingType.*;
import static com.example.cafekiosk.spring.domain.product.ProductType.*;


/**
 * readOnly = true : 읽기 전용
 * CRUD 중 CUD 동작 X / only Read
 * JPA : CUD 스냅샷 저장, 변경감지 X (성능 향상)
 *
 * CQRS = Command / Query
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingTypeIn(forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }


    // 동시성 이슈
    // 사람이 많지않으면 혹은 빈도가 많지 않으면 그냥 재시도 로직정도 추가 가능
    // 동시에 등록하는 사람이 너무많다면 UUID 로 변경하는 등 정책변경 고려 가능
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product saved = productRepository.save(product);

        return ProductResponse.of(saved);
    }

    private String createNextProductNumber() {
        // productNumber
        // DB 에서 마지막 저장된 프로덕트의 상품번호 +1
        String latestProductNumber = productRepository.findLatestProductNumber();

        if(latestProductNumber==null) return "001";

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }
}
