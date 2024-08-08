package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.controller.dto.request.PurchaseModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PurchaseInfoResponse;
import com.bcsd.shop.service.PurchaseService;
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

import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "주문", description = "주문 관련 API")
@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Operation(summary = "특정 주문번호의 주문정보 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseInfoResponse> getPurchase(
            @Parameter(in = PATH) @PathVariable Long id,
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId
    ) {
        PurchaseInfoResponse response = purchaseService.getPurchaseInfo(id, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 상품번호의 모든 주문정보 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PurchaseInfoResponse>> getPurchasesByProduct(
            @Parameter(in = PATH) @PathVariable Long productId,
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId
    ) {
        List<PurchaseInfoResponse> responses = purchaseService.getPurchasesByProductId(productId, userId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "본인의 주문목록 전체 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/user")
    public ResponseEntity<List<PurchaseInfoResponse>> getPurchaseByUser(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId
    ) {
        List<PurchaseInfoResponse> responses = purchaseService.getPurchasesByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "주문 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping
    public ResponseEntity<PurchaseInfoResponse> createPurchase(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid PurchaseCreateRequest request
    ) {
        PurchaseInfoResponse response = purchaseService.createPurchase(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "주문 상태 변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<PurchaseInfoResponse> modifyStatusPurchase(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @Parameter(in = PATH) @PathVariable Long id,
            @RequestBody @Valid PurchaseModifyStatusRequest request
    ) {
        PurchaseInfoResponse response = purchaseService.modifyStatusPurchase(userId, id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 취소")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PutMapping("/{id}/cancel")
    public ResponseEntity<PurchaseInfoResponse> cancelPurchase(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @Parameter(in = PATH) @PathVariable Long id
    ) {
        PurchaseInfoResponse response = purchaseService.cancelPurchase(userId, id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "구매 결정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PutMapping("/{id}/determine")
    public ResponseEntity<PurchaseInfoResponse> determinePurchase(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @Parameter(in = PATH) @PathVariable Long id,
            @RequestBody @Valid PurchaseModifyStatusRequest request
    ) {
        PurchaseInfoResponse response = purchaseService.modifyStatusPurchaseUser(userId, id, request);
        return ResponseEntity.ok(response);
    }
}
