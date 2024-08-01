package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.domain.ProductStatus;
import com.bcsd.shop.domain.Sorter;
import com.bcsd.shop.exception.CustomException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import static com.bcsd.shop.exception.errorcode.PaymentErrorCode.INVALID_PRICE_RANGE;

public record ProductSearchRequest(
        @Size(max = 255, message = "검색어는 최대 255자까지 입력할 수 있습니다.")
        String q,

        Long categoryId,

        @Min(value = 0, message = "최소 가격은 0이상이어야 합니다.")
        Long minPrice,

        @Min(value = 0, message = "최대 가격은 0이상이어야 합니다")
        Long maxPrice,

        ProductStatus status,

        @Min(value = 0, message = "페이지 번호는 0이상이어야 합니다")
        Integer page,

        @Min(value = 1, message = "페이지 크기는 1이상이어야 합니다")
        Integer listSize,

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
