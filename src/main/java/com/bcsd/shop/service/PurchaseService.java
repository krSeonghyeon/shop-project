package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.controller.dto.response.PurchaseInfoResponse;

public interface PurchaseService {

    PurchaseInfoResponse createPurchase(Long userId, PurchaseCreateRequest request);
}
