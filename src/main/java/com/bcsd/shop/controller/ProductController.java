package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.request.ProductModifyRequest;
import com.bcsd.shop.controller.dto.request.ProductSearchRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;
import com.bcsd.shop.controller.dto.response.ProductSimpleInfoResponse;
import com.bcsd.shop.service.ProductService;
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

@Tag(name = "상품", description = "상품 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품검색")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<ProductSimpleInfoResponse>> searchProducts(
            @ModelAttribute @Valid ProductSearchRequest request
    ) {
        List<ProductSimpleInfoResponse> response = productService.searchProducts(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 상품 정보 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductInfoResponse> getProduct(@Parameter(in = PATH) @PathVariable Long id) {
        ProductInfoResponse response = productService.getProductInfo(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "본인이 등록한 상품목록 전체조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping
    public ResponseEntity<List<ProductInfoResponse>> getProducts(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId
    ) {
        List<ProductInfoResponse> response = productService.getProductsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품등록")
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
    public ResponseEntity<ProductInfoResponse> createProduct(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid ProductCreateRequest request
    ) {
        ProductInfoResponse response = productService.createProduct(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "상품정보 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductInfoResponse> modifyProduct(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @Parameter(in = PATH) @PathVariable Long id,
            @RequestBody @Valid ProductModifyRequest request
    ) {
        ProductInfoResponse response = productService.modifyProduct(userId, id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @Parameter(in = PATH) @PathVariable Long id
    ) {
        productService.deleteProduct(userId, id);
        return ResponseEntity.noContent().build();
    }
}
