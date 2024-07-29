package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;
import com.bcsd.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductInfoResponse> createProduct(
            @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid ProductCreateRequest request
    ) {
        ProductInfoResponse response = productService.createProduct(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
