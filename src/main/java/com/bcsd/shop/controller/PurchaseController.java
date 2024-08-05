package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.controller.dto.request.PurchaseModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PurchaseInfoResponse;
import com.bcsd.shop.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseInfoResponse> getPurchase(
            @PathVariable Long id,
            @SessionAttribute(name = "userId") Long userId
    ) {
        PurchaseInfoResponse response = purchaseService.getPurchaseInfo(id, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PurchaseInfoResponse>> getPurchasesByProduct(
            @PathVariable Long productId,
            @SessionAttribute(name = "userId") Long userId
    ) {
        List<PurchaseInfoResponse> responses = purchaseService.getPurchasesByProductId(productId, userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<PurchaseInfoResponse> createPurchase(
            @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid PurchaseCreateRequest request
    ) {
        PurchaseInfoResponse response = purchaseService.createPurchase(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PurchaseInfoResponse> modifyStatusPurchase(
            @SessionAttribute(name = "userId") Long userId,
            @PathVariable Long id,
            @RequestBody @Valid PurchaseModifyStatusRequest request
    ) {
        PurchaseInfoResponse response = purchaseService.modifyStatusPurchase(userId, id, request);
        return ResponseEntity.ok(response);
    }
}
