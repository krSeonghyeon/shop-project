package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;
import com.bcsd.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductInfoResponse> getProduct(@PathVariable Long id) {
        ProductInfoResponse response = productService.getProductInfo(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductInfoResponse> createProduct(
            @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid ProductCreateRequest request
    ) {
        ProductInfoResponse response = productService.createProduct(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
