package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.controller.dto.request.PurchaseModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PurchaseInfoResponse;
import com.bcsd.shop.domain.*;
import com.bcsd.shop.exception.CustomException;
import com.bcsd.shop.repository.PaymentRepository;
import com.bcsd.shop.repository.ProductRepository;
import com.bcsd.shop.repository.PurchaseRepository;
import com.bcsd.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bcsd.shop.exception.errorcode.AuthErrorCode.FORBIDDEN_PRODUCT;
import static com.bcsd.shop.exception.errorcode.AuthErrorCode.FORBIDDEN_PURCHASE;
import static com.bcsd.shop.exception.errorcode.PaymentErrorCode.PAYMENT_NOT_FOUND;
import static com.bcsd.shop.exception.errorcode.ProductErrorCode.INVALID_PRODUCT_STATUS;
import static com.bcsd.shop.exception.errorcode.ProductErrorCode.PRODUCT_NOT_FOUND;
import static com.bcsd.shop.exception.errorcode.PurchaseErrorCode.*;
import static com.bcsd.shop.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public List<PurchaseInfoResponse> getPurchasesByUserId(Long userId) {
        List<Purchase> purchases = purchaseRepository.findAllByUserId(userId);
        return purchases.stream()
                .map(PurchaseInfoResponse::from)
                .toList();
    }

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

    @Transactional
    public PurchaseInfoResponse createPurchase(Long userId, PurchaseCreateRequest request) {
        try {
            if (purchaseRepository.existsByPaymentId(request.paymentId())) {
                throw new CustomException(PURCHASE_DUPLICATED);
            }

            Product product = productRepository.findByIdForUpdate(request.productId())
                    .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

            User seller = product.getSeller();

            Payment payment = paymentRepository.findById(request.paymentId())
                    .orElseThrow(() -> new CustomException(PAYMENT_NOT_FOUND));

            if (product.getStatus() != ProductStatus.판매중) {
                throw new CustomException(INVALID_PRODUCT_STATUS);
            }

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
        } catch (CustomException e) {
            handlePaymentCancel(request.paymentId());
            throw e;
        } catch (Exception e) {
            log.error("주문에서 예상치 못한 예외가 발생했습니다. 요청 정보: {}, 에러 메시지: {}", request, e.getMessage());
            handlePaymentCancel(request.paymentId());
            throw new CustomException(FAILED_PURCHASE);
        }
    }

    private void handlePaymentCancel(Long paymentId) {
        try {
            paymentService.cancelPayment(paymentId);
        } catch (Exception ex) {
            log.error("결제 취소에 실패했습니다. 결제ID: {}", paymentId, ex);
        }
    }

    @Transactional
    public PurchaseInfoResponse modifyStatusPurchase(Long userId, Long id, PurchaseModifyStatusRequest request) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));

        User seller = purchase.getSeller();

        if (!seller.getId().equals(userId)) {
            throw new CustomException(FORBIDDEN_PRODUCT);
        }

        PurchaseStatus status = PurchaseStatus.valueOf(request.status());

        if (purchase.getStatus() == status) {
            throw new CustomException(INVALID_SAME_PURCHASE_STATUS);
        }

        purchase.changeStatus(status);

        Purchase updatedPurchase = purchaseRepository.saveAndRefresh(purchase);

        return PurchaseInfoResponse.from(updatedPurchase);
    }

    @Transactional
    public PurchaseInfoResponse cancelPurchase(Long userId, Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));

        User user = purchase.getUser();

        if (!user.getId().equals(userId)) {
            throw new CustomException(FORBIDDEN_PURCHASE);
        }

        if (purchase.getStatus() != PurchaseStatus.결제완료) {
            throw new CustomException(INVALID_PURCHASE_CANCEL);
        }

        purchase.changeStatus(PurchaseStatus.취소요청);

        Purchase updatedPurchase = purchaseRepository.saveAndRefresh(purchase);

        return PurchaseInfoResponse.from(updatedPurchase);
    }

    @Transactional
    public PurchaseInfoResponse modifyStatusPurchaseUser(Long userId, Long id, PurchaseModifyStatusRequest request) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));

        User user = purchase.getUser();

        if (!user.getId().equals(userId)) {
            throw new CustomException(FORBIDDEN_PURCHASE);
        }

        if (purchase.getStatus() != PurchaseStatus.배송중) {
            throw new CustomException(INVALID_PURCHASE_DETERMINE);
        }

        PurchaseStatus status = PurchaseStatus.valueOf(request.status());

        if (status != PurchaseStatus.반품요청
                && status != PurchaseStatus.교환요청
                && status != PurchaseStatus.구매확정) {
            throw new CustomException(INVALID_PURCHASE_STATUS);
        }

        purchase.changeStatus(status);

        Purchase updatedPurchase = purchaseRepository.saveAndRefresh(purchase);

        return PurchaseInfoResponse.from(updatedPurchase);
    }
}
