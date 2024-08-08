package com.bcsd.shop.controller.dto.response;

import java.util.List;

public record ProductSearchResponse(
        List<ProductSimpleInfoResponse> products,
        int currentPage,
        int totalPage,
        long totalCount,
        int pageSize
) {
}
