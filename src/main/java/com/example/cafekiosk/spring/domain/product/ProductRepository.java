package com.example.cafekiosk.spring.domain.product;

import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     *  select *
     *  from product
     *  where selling_type in ('SELLING', 'HOLD)
     */
    List<Product> findAllBySellingTypeIn(List<ProductSellingType> sellingTypes);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    @Query(value = "select p.product_number from product p order by id desc limit 1", nativeQuery = true)
    String findLatestProductNumber();
}
