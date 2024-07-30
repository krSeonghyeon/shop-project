package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.request.ProductModifyRequest;
import com.bcsd.shop.controller.dto.request.ProductSearchRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;
import com.bcsd.shop.controller.dto.response.ProductSimpleInfoResponse;

import java.util.List;

public interface ProductService {

    List<ProductSimpleInfoResponse> searchProducts(ProductSearchRequest request);
    List<ProductInfoResponse> getProductsByUserId(Long userId);
    ProductInfoResponse getProductInfo(Long id);
    ProductInfoResponse createProduct(Long userId, ProductCreateRequest request);
    ProductInfoResponse modifyProduct(Long userId, Long id, ProductModifyRequest request);
    void deleteProduct(Long userId, Long id);
}
