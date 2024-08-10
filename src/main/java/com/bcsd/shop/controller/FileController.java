package com.bcsd.shop.controller;

import com.bcsd.shop.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "이미지", description = "이미지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "이미지조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@Parameter(in = PATH) @PathVariable String fileName) {
        Resource resource = fileService.getImage(fileName);
        String contentType = fileService.determineContentType(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
