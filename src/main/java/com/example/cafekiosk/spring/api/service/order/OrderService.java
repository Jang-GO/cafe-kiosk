package com.example.cafekiosk.spring.api.service.order;

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.example.cafekiosk.spring.domain.order.Order;
import com.example.cafekiosk.spring.domain.order.OrderRepository;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductType;
import com.example.cafekiosk.spring.domain.stock.Stock;
import com.example.cafekiosk.spring.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> numbers = request.getProductNumbers();
        List<Product> duplicateProducts = findProductsBy(numbers);

        // 재고 차감 체크가 필요한 상품들 필터링
        List<String> stockProductNumbers = duplicateProducts.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
        // 재고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(numbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
        // 상품별 counting
        Map<String, Long> productContingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        // 재고 차감 시도
        for(String stockProductNumber:new HashSet<>(stockProductNumbers)){
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productContingMap.get(stockProductNumber).intValue();
            if(stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }


        Order order = Order.create(duplicateProducts, registeredDateTime);
        Order saved = orderRepository.save(order);

        OrderResponse orderResponse = OrderResponse.of(saved);
        return orderResponse;
    }

    private List<Product> findProductsBy(List<String> numbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(numbers);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, a -> a));

        List<Product> duplicateProducts = numbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
        return duplicateProducts;
    }
}
