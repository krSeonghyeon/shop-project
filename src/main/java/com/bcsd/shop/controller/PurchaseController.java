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

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

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
