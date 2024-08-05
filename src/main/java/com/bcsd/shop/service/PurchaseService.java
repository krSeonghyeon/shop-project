package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.controller.dto.request.PurchaseModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PurchaseInfoResponse;

public interface PurchaseService {

    PurchaseInfoResponse getPurchaseInfo(Long id, Long userId);
    PurchaseInfoResponse createPurchase(Long userId, PurchaseCreateRequest request);
    PurchaseInfoResponse modifyStatusPurchase(Long userId, Long id, PurchaseModifyStatusRequest request);
}
