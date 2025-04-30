package com.neighboursnack.productservice.dto;

import com.neighboursnack.common.util.AppUtil;
import com.neighboursnack.productservice.entity.Product;
import com.neighboursnack.productservice.entity.ProductVariant;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductDTO {

    public record ProductRequestDTO(
            @NotBlank(message = "Product name is required")
            @Size(min = 3, max = 150, message = "Product name must be between 3 and 150 characters")
            @Pattern(regexp = "^[A-Za-z0-9\\-'/()& ]+$", message = "Invalid characters in product name")
            String name,

            @Size(max = 500, message = "Description must not exceed 500 characters")
            String description,

            @NotNull(message = "Category UUID is required")
            UUID categoryUuid,

            @NotEmpty(message = "At least one variant is required")
            @Size(max = 10, message = "Maximum of 10 variants allowed")
            List<ProductVariantRequestDTO> variants
    ) {
        public Product toEntity() {
            Product product = Product.builder()
                    .name(name.toLowerCase())
                    .normalizedName(AppUtil.normalizeName(name))
                    .description(description)
                    .categoryUuid(categoryUuid)
                    .build();

            List<ProductVariant> variantEntities = variants.stream()
                    .map(v -> v.toEntity(product))
                    .collect(Collectors.toList());

            product.setVariants(variantEntities);
            return product;
        }

        public Product applyUpdatesTo(Product product) {
            product.setName(name.toLowerCase());
            product.setNormalizedName(AppUtil.normalizeName(name));
            product.setDescription(description);
            product.setCategoryUuid(categoryUuid);

            product.getVariants().clear();
            product.getVariants().addAll(
                    variants.stream().map(v -> v.toEntity(product)).toList()
            );
            return product;
        }
    }

    public record ProductVariantRequestDTO(
            @NotBlank(message = "Pack size is required")
            @Size(max = 20, message = "Pack size must not exceed 20 characters")
            String packSize,

            @NotNull(message = "Price is required")
            @Positive(message = "Price must be positive")
            @Digits(integer = 10, fraction = 2, message = "Price must have at most 2 decimal places")
            BigDecimal price,

            @NotNull(message = "Stock status is required")
            Boolean inStock
    ) {
        public ProductVariant toEntity(Product product) {
            return ProductVariant.builder()
                    .packSize(packSize)
                    .price(price)
                    .inStock(inStock)
                    .product(product)
                    .build();
        }
    }

    public record ProductVariantResponseDTO(
            UUID uuid,
            String packSize,
            BigDecimal price,
            boolean inStock
    ) {
        public static ProductVariantResponseDTO fromEntity(ProductVariant variant) {
            return new ProductVariantResponseDTO(
                    variant.getUuid(),
                    variant.getPackSize(),
                    variant.getPrice(),
                    variant.getInStock()
            );
        }
    }

    public record ProductResponseDTO(
            UUID uuid,
            String name,
            String normalizedName,
            String description,
            String productImage,
            String categoryName,
            UUID categoryUuid,
            List<ProductVariantResponseDTO> variants
    ) {
        public static ProductResponseDTO fromEntity(Product product, String categoryName) {
            return new ProductResponseDTO(
                    product.getUuid(),
                    product.getName(),
                    product.getNormalizedName(),
                    product.getDescription(),
                    product.getProductImage(),
                    categoryName,
                    product.getCategoryUuid(),
                    product.getVariants().stream()
                            .map(ProductVariantResponseDTO::fromEntity)
                            .collect(Collectors.toList())
            );
        }
    }

}

