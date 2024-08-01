package com.example.cafekiosk.spring.api.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    // 서비스레이어 하위로는 다 Mocking 처리할 것임
    @Autowired
    private MockMvc mockMvc;

}