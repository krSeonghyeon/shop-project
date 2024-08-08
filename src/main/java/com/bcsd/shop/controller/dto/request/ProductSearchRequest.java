package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.domain.ProductStatus;
import com.bcsd.shop.domain.Sorter;
import com.bcsd.shop.exception.CustomException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import static com.bcsd.shop.exception.errorcode.PaymentErrorCode.INVALID_PRICE_RANGE;

public record ProductSearchRequest(
        @Schema(example = "맥북", description = "검색어(상품명)")
        @Size(max = 255, message = "검색어는 최대 255자까지 입력할 수 있습니다.")
        String q,

        @Schema(example = "2", description = "카테고리 번호(필터)")
        Long categoryId,

        @Schema(example = "1000000", description = "최소 가격(필터)")
        @Min(value = 0, message = "최소 가격은 0이상이어야 합니다.")
        Long minPrice,

        @Schema(example = "2000000", description = "최대 가격(필터)")
        @Min(value = 0, message = "최대 가격은 0이상이어야 합니다")
        Long maxPrice,

        @Schema(example = "판매중", description = "상품 상태(필터) -> \"판매예정\" 또는 \"판매중\" 또는 \"판매중지\" 또는 \"품절\"")
        ProductStatus status,

        @Schema(example = "0", description = "페이지 번호")
        @Min(value = 0, message = "페이지 번호는 0이상이어야 합니다")
        Integer page,

        @Schema(example = "10", description = "페이지 크기")
        @Min(value = 1, message = "페이지 크기는 1이상이어야 합니다")
        Integer listSize,

        @Schema(example = "salePriceAsc", description = "정렬조건 -> \"salePriceAsc\" 또는 \"salePriceDesc\" 또는 \"latest\"")
        String sorter
) {
    public ProductSearchRequest {
        if (page == null) page = 0;
        if (listSize == null) listSize = 10;
        if (sorter == null) sorter = Sorter.SALE_PRICE_ASC.getValue();
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new CustomException(INVALID_PRICE_RANGE);
        }
    }

    public Sorter getSorter() {
        return Sorter.fromValue(sorter);
    }
}
