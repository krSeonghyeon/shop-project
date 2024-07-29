package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;

public interface ProductService {

    ProductInfoResponse getProductInfo(Long id);
    ProductInfoResponse createProduct(Long userId, ProductCreateRequest request);
}
