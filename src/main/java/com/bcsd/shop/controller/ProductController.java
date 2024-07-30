package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.request.ProductModifyRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;
import com.bcsd.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ProductInfoResponse>> getProducts(
            @SessionAttribute(name = "userId") Long userId
    ) {
        List<ProductInfoResponse> response = productService.getProductsByUserId(userId);
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

    @PutMapping("/{id}")
    public ResponseEntity<ProductInfoResponse> modifyProduct(
            @SessionAttribute(name = "userId") Long userId,
            @PathVariable Long id,
            @RequestBody @Valid ProductModifyRequest request
    ) {
        ProductInfoResponse response = productService.modifyProduct(userId, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @SessionAttribute(name = "userId") Long userId,
            @PathVariable Long id
    ) {
        productService.deleteProduct(userId, id);
        return ResponseEntity.noContent().build();
    }
}
