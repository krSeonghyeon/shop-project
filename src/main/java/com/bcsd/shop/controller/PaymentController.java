package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.PaymentCreateRequest;
import com.bcsd.shop.controller.dto.request.PaymentModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PaymentInfoResponse;
import com.bcsd.shop.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "결제", description = "결제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PaymentInfoResponse> getPayment(@Parameter(in = PATH) @PathVariable Long id) {
        PaymentInfoResponse response = paymentService.getPaymentInfo(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결제 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping
    public ResponseEntity<PaymentInfoResponse> createPayment(
            @RequestBody @Valid PaymentCreateRequest request
    ) {
        PaymentInfoResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "결제 취소")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PutMapping("/{id}/cancel")
    public ResponseEntity<PaymentInfoResponse> cancelPayment(@Parameter(in = PATH) @PathVariable Long id) {
        PaymentInfoResponse response = paymentService.cancelPayment(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결제 상태변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentInfoResponse> modifyStatusPayment(
            @Parameter(in = PATH) @PathVariable Long id,
            @RequestBody @Valid PaymentModifyStatusRequest request
    ) {
        PaymentInfoResponse response = paymentService.modifyStatusPayment(id, request);
        return ResponseEntity.ok(response);
    }
}
