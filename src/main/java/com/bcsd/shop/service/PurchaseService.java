package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.controller.dto.request.PurchaseModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PurchaseInfoResponse;

import java.util.List;

public interface PurchaseService {

    List<PurchaseInfoResponse> getPurchasesByUserId(Long userId);
    List<PurchaseInfoResponse> getPurchasesByProductId(Long productId, Long userId);
    PurchaseInfoResponse getPurchaseInfo(Long id, Long userId);
    PurchaseInfoResponse createPurchase(Long userId, PurchaseCreateRequest request);
    PurchaseInfoResponse modifyStatusPurchase(Long userId, Long id, PurchaseModifyStatusRequest request);
    PurchaseInfoResponse cancelPurchase(Long userId, Long id);
}
