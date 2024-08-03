package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.request.ProductModifyRequest;
import com.bcsd.shop.controller.dto.request.ProductSearchRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;
import com.bcsd.shop.controller.dto.response.ProductSimpleInfoResponse;
import com.bcsd.shop.domain.*;
import com.bcsd.shop.exception.CustomException;
import com.bcsd.shop.repository.CategoryRepository;
import com.bcsd.shop.repository.ProductRepository;
import com.bcsd.shop.repository.UserRepository;
import com.bcsd.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bcsd.shop.exception.errorcode.AuthErrorCode.FORBIDDEN_PRODUCT;
import static com.bcsd.shop.exception.errorcode.ProductErrorCode.CATEGORY_NOT_FOUND;
import static com.bcsd.shop.exception.errorcode.ProductErrorCode.PRODUCT_NOT_FOUND;
import static com.bcsd.shop.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<ProductSimpleInfoResponse> searchProducts(ProductSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.page(), request.listSize(),
                getSort(request.getSorter())
        );

        Specification<Product> specification = Specification.where(nameContains(request.q()))
                .and(categoryEquals(request.categoryId()))
                .and(priceBetween(request.minPrice(), request.maxPrice()))
                .and(statusEquals(request.status()));

        List<Product> products = productRepository.findAll(specification, pageable).getContent();

        return products.stream()
                .map(ProductSimpleInfoResponse::from)
                .toList();
    }

    private Sort getSort(Sorter sorter) {
        return switch (sorter) {
            case SALE_PRICE_ASC -> Sort.by(Sort.Direction.ASC, "price");
            case SALE_PRICE_DESC -> Sort.by(Sort.Direction.DESC, "price");
            case LATEST -> Sort.by(Sort.Direction.DESC, "updatedAt");
        };
    }

    private Specification<Product> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    private Specification<Product> categoryEquals(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    private Specification<Product> priceBetween(Long minPrice, Long maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            } else {
                return null;
            }
        };
    }

    private Specification<Product> statusEquals(ProductStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    @Override
    public List<ProductInfoResponse> getProductsByUserId(Long userId) {
        List<Product> products = productRepository.findAllBySellerId(userId);
        return products.stream()
                .map(ProductInfoResponse::from)
                .toList();
    }

    @Override
    public ProductInfoResponse getProductInfo(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        return ProductInfoResponse.from(product);
    }

    @Override
    @Transactional
    public ProductInfoResponse createProduct(Long userId, ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

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

        return ProductInfoResponse.from(savedProduct);
    }

    @Override
    @Transactional
    public ProductInfoResponse modifyProduct(Long userId, Long id, ProductModifyRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        User seller = product.getSeller();

        if (!seller.getId().equals(userId)) {
            throw new CustomException(FORBIDDEN_PRODUCT);
        }

        Category newCategory = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));

        product.changeProductInfo(
                newCategory,
                request.name(),
                request.image(),
                request.description(),
                request.price(),
                request.shippingCost(),
                request.stock(),
                request.status() //유효하지않은 enum은 json파싱단계에서 HttpMessageNotReadableException발생
        );

        Product updatedProduct = productRepository.saveAndRefresh(product);

        return ProductInfoResponse.from(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long userId, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        User seller = product.getSeller();

        if (!seller.getId().equals(userId)) {
            throw new CustomException(FORBIDDEN_PRODUCT);
        }

        productRepository.deleteById(id);
    }
}
