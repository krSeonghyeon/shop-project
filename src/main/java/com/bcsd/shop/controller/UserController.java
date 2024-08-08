package com.bcsd.shop.controller;

import com.bcsd.shop.controller.dto.request.PasswordModifyRequest;
import com.bcsd.shop.controller.dto.request.SellerJoinRequest;
import com.bcsd.shop.controller.dto.request.UserInfoModifyRequest;
import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.controller.dto.response.SellerInfoResponse;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.service.UserService;
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

@Tag(name = "회원", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "본인정보 조회(일반회원)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @Operation(summary = "본인정보 조회(판매자회원)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/seller")
    public ResponseEntity<SellerInfoResponse> getSellerInfo(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(userService.getSellerInfo(userId));
    }

    @Operation(summary = "회원가입(일반)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping
    public ResponseEntity<UserInfoResponse> joinUser(@RequestBody @Valid UserJoinRequest request) {
        UserInfoResponse response = userService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "회원가입(판매자)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping("/seller")
    public ResponseEntity<SellerInfoResponse> joinSeller(@RequestBody @Valid SellerJoinRequest request) {
        SellerInfoResponse response = userService.joinSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "비밀번호 변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PutMapping("/password")
    public ResponseEntity<Void> modifyPassword(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid PasswordModifyRequest request
    ) {
        userService.modifyPassword(userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원정보 변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PutMapping("/info")
    public ResponseEntity<UserInfoResponse> modifyUserInfo(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId,
            @RequestBody @Valid UserInfoModifyRequest request
    ) {
        UserInfoResponse response = userService.modifyUserInfo(userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원탈퇴")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204"),
                    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @Parameter(hidden = true) @SessionAttribute(name = "userId") Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
