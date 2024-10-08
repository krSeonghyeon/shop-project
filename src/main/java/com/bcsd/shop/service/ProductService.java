package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.ProductCreateRequest;
import com.bcsd.shop.controller.dto.request.ProductModifyRequest;
import com.bcsd.shop.controller.dto.request.ProductSearchRequest;
import com.bcsd.shop.controller.dto.response.ProductInfoResponse;
import com.bcsd.shop.controller.dto.response.ProductSearchResponse;
import com.bcsd.shop.controller.dto.response.ProductSimpleInfoResponse;
import com.bcsd.shop.domain.*;
import com.bcsd.shop.exception.CustomException;
import com.bcsd.shop.repository.CategoryRepository;
import com.bcsd.shop.repository.ProductRepository;
import com.bcsd.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bcsd.shop.exception.errorcode.AuthErrorCode.FORBIDDEN_PRODUCT;
import static com.bcsd.shop.exception.errorcode.ProductErrorCode.*;
import static com.bcsd.shop.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public ProductSearchResponse searchProducts(ProductSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.page(), request.listSize(),
                getSort(request.getSorter())
        );

        Specification<Product> specification = Specification.where(nameContains(request.q()))
                .and(categoryEquals(request.categoryId()))
                .and(priceBetween(request.minPrice(), request.maxPrice()))
                .and(statusEquals(request.status()));

        Page<Product> productPage = productRepository.findAll(specification, pageable);
        List<ProductSimpleInfoResponse> responses = productPage.getContent().stream()
                .map(ProductSimpleInfoResponse::from)
                .toList();

        return new ProductSearchResponse(
                responses,
                productPage.getNumber(),
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.getSize()
        );
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

    public List<ProductInfoResponse> getProductsByUserId(Long userId) {
        List<Product> products = productRepository.findAllBySellerId(userId);
        return products.stream()
                .map(ProductInfoResponse::from)
                .toList();
    }

    public ProductInfoResponse getProductInfo(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        return ProductInfoResponse.from(product);
    }

    @Transactional
    public ProductInfoResponse createProduct(Long userId, ProductCreateRequest request) {
        if (productRepository.existsBySellerIdAndName(userId, request.name())) {
            throw new CustomException(PRODUCT_DUPLICATED);
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String imageUrl = null;
        if (request.image() != null) {
            imageUrl = fileService.saveImage(request.image());
        }

        Product newProduct = Product.builder()
                .category(category)
                .seller(user)
                .name(request.name())
                .image(imageUrl)
                .description(request.description())
                .price(request.price())
                .shippingCost(request.shippingCost())
                .stock(request.stock())
                .build();

        Product savedProduct = productRepository.saveAndRefresh(newProduct);

        return ProductInfoResponse.from(savedProduct);
    }

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

        String newImageUrl = product.getImage();

        if (request.image() != null) {
            if (newImageUrl != null) {
                fileService.deleteImage(newImageUrl);
            }
            newImageUrl = fileService.saveImage(request.image());
        }

        product.changeProductInfo(
                newCategory,
                request.name(),
                newImageUrl,
                request.description(),
                request.price(),
                request.shippingCost(),
                request.stock(),
                ProductStatus.valueOf(request.status())
        );

        Product updatedProduct = productRepository.saveAndRefresh(product);

        return ProductInfoResponse.from(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long userId, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        User seller = product.getSeller();

        if (!seller.getId().equals(userId)) {
            throw new CustomException(FORBIDDEN_PRODUCT);
        }

        if (product.getImage() != null) {
            fileService.deleteImage(product.getImage());
        }

        productRepository.deleteById(id);
    }
}
