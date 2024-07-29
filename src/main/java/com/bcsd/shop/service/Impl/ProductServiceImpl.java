package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;
import com.bcsd.shop.domain.Category;
import com.bcsd.shop.domain.Product;
import com.bcsd.shop.domain.User;
import com.bcsd.shop.repository.CategoryRepository;
import com.bcsd.shop.repository.ProductRepository;
import com.bcsd.shop.repository.UserRepository;
import com.bcsd.shop.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public ProductInfoResponse getProductInfo(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다"));

        User seller = product.getSeller();
        Category category = product.getCategory();

        return ProductInfoResponse.of(product, seller, category);
    }

    @Override
    @Transactional
    public ProductInfoResponse createProduct(Long userId, ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다"));

        Product newProduct = Product.builder()
                .category(category)
                .seller(user)
                .name(request.name())
                .image(request.image())
                .description(request.description())
                .price(request.price())
                .shippingCost(request.shippingCost())
                .stock(request.stock())
                .build();

        Product savedProduct = productRepository.saveAndRefresh(newProduct);

        return ProductInfoResponse.of(savedProduct, user, category);
    }

    @Override
    @Transactional
    public void deleteProduct(Long userId, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다"));

        User seller = product.getSeller();

        if (!seller.getId().equals(userId)) {
            throw new AccessDeniedException("해당 상품의 판매자가 아닙니다");
        }

        productRepository.deleteById(id);
    }
}
