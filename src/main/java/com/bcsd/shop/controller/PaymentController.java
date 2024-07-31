package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.PaymentCreateRequest;
import com.bcsd.shop.controller.dto.request.PaymentModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PaymentInfoResponse;
import com.bcsd.shop.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentInfoResponse> getPayment(@PathVariable Long id) {
        PaymentInfoResponse response = paymentService.getPaymentInfo(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PaymentInfoResponse> createPayment(
            @RequestBody @Valid PaymentCreateRequest request
    ) {
        PaymentInfoResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<PaymentInfoResponse> cancelPayment(@PathVariable Long id) {
        PaymentInfoResponse response = paymentService.cancelPayment(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentInfoResponse> modifyStatusPayment(
            @PathVariable Long id,
            @RequestBody @Valid PaymentModifyStatusRequest request
    ) {
        PaymentInfoResponse response = paymentService.modifyStatusPayment(id, request);
        return ResponseEntity.ok(response);
    }
}
