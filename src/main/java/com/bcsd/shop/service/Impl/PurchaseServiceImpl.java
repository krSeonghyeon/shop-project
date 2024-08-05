package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.controller.dto.request.PurchaseModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PurchaseInfoResponse;
import com.bcsd.shop.domain.*;
import com.bcsd.shop.exception.CustomException;
import com.bcsd.shop.repository.PaymentRepository;
import com.bcsd.shop.repository.ProductRepository;
import com.bcsd.shop.repository.PurchaseRepository;
import com.bcsd.shop.repository.UserRepository;
import com.bcsd.shop.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bcsd.shop.exception.errorcode.AuthErrorCode.FORBIDDEN_PRODUCT;
import static com.bcsd.shop.exception.errorcode.AuthErrorCode.FORBIDDEN_PURCHASE;
import static com.bcsd.shop.exception.errorcode.PaymentErrorCode.PAYMENT_NOT_FOUND;
import static com.bcsd.shop.exception.errorcode.ProductErrorCode.PRODUCT_NOT_FOUND;
import static com.bcsd.shop.exception.errorcode.PurchaseErrorCode.*;
import static com.bcsd.shop.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public List<PurchaseInfoResponse> getPurchasesByUserId(Long userId) {
        List<Purchase> purchases = purchaseRepository.findAllByUserId(userId);
        return purchases.stream()
                .map(PurchaseInfoResponse::from)
                .toList();
    }

    @Override
    public List<PurchaseInfoResponse> getPurchasesByProductId(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        User seller = product.getSeller();

        if (!seller.getId().equals(userId)) {
            throw new CustomException(FORBIDDEN_PRODUCT);
        }

        List<Purchase> purchases = purchaseRepository.findAllByProductId(productId);

        return purchases.stream()
                .map(PurchaseInfoResponse::from)
                .toList();
    }

    @Override
    public PurchaseInfoResponse getPurchaseInfo(Long id, Long userId) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));

        User seller = purchase.getSeller();
        User user = purchase.getUser();

        if (!userId.equals(user.getId()) && !userId.equals(seller.getId())) {
            throw new CustomException(FORBIDDEN_PURCHASE);
        }

        return PurchaseInfoResponse.from(purchase);
    }

    @Override
    @Transactional
    public PurchaseInfoResponse createPurchase(Long userId, PurchaseCreateRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        User seller = product.getSeller();

        Payment payment = paymentRepository.findById(request.paymentId())
                .orElseThrow(() -> new CustomException(PAYMENT_NOT_FOUND));

        if (request.quantity() > product.getStock()) {
            throw new CustomException(INVALID_OVER_STOCK);
        }

        product.decreaseStock(request.quantity());

        if (product.getStock() == 0) {
            product.changeStatus(ProductStatus.품절);
        }

        Purchase purchase = Purchase.builder()
                .product(product)
                .user(user)
                .seller(seller)
                .payment(payment)
                .price(product.getPrice())
                .quantity(request.quantity())
                .shippingCost(product.getShippingCost())
                .shippingAddress(user.getAddress())
                .request(request.request())
                .build();

        Purchase savedPurchase = purchaseRepository.saveAndRefresh(purchase);

        return PurchaseInfoResponse.from(savedPurchase);
    }

    @Override
    @Transactional
    public PurchaseInfoResponse modifyStatusPurchase(Long userId, Long id, PurchaseModifyStatusRequest request) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));

        User seller = purchase.getSeller();

        if (!seller.getId().equals(userId)) {
            throw new CustomException(FORBIDDEN_PRODUCT);
        }

        if (purchase.getStatus() == request.status()) {
            throw new CustomException(INVALID_SAME_PURCHASE_STATUS);
        }

        purchase.changeStatus(request.status());

        Purchase updatedPurchase = purchaseRepository.saveAndRefresh(purchase);

        return PurchaseInfoResponse.from(updatedPurchase);
    }
}
