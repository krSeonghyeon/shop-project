package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.request.ProductModifyRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;

public interface ProductService {

    ProductInfoResponse getProductInfo(Long id);
    ProductInfoResponse createProduct(Long userId, ProductCreateRequest request);
    ProductInfoResponse modifyProduct(Long userId, Long id, ProductModifyRequest request);
    void deleteProduct(Long userId, Long id);
}
