package com.neighboursnack.productservice.service.impl;

import com.neighboursnack.productservice.dto.ProductDTO.ProductRequestDTO;
import com.neighboursnack.productservice.dto.ProductDTO.ProductResponseDTO;
import com.neighboursnack.productservice.entity.Product;
import com.neighboursnack.productservice.repository.ProductRepository;
import com.neighboursnack.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.neighboursnack.common.util.AppConstant.IMAGE_DIRECTORY;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponseDTO> getAllProductsByCategoryId(UUID categoryId) {
        return productRepository.findAll().stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponseDTO createProductInCategory(UUID categoryId, ProductRequestDTO productRequest) {
        Category category = categoryRepository.findCategoryByUuid(categoryId);

        // Check if product already exists
        if (productRepository.existsByName(productRequest.name().toLowerCase())) {
            throw new IllegalArgumentException("Product with this name already exists");
        }

        Product savedProduct = productRepository.save(productRequest.toEntity(category));
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    @Override
    public ProductResponseDTO uploadProductImage(UUID categoryId, UUID productId, MultipartFile file) {
        Category category = categoryRepository.findCategoryByUuid(categoryId);
        Product product = productRepository.findProductByUuid(productId);

        // Validate if image is present
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }

        // Validate file size (Max: 2MB)
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("Image size must not exceed 2MB");
        }

        // Validate file type (only allow JPEG, PNG)
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Only JPEG and PNG images are allowed");
        }

        // Extract file extension dynamically
        String originalFileName = file.getOriginalFilename();
        String fileExtension = (originalFileName != null && originalFileName.contains("."))
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : ".jpg"; // Default to .jpg if no extension is found

        // Generate new file name using product's normalized name
        String normalizedFileName = product.getNormalizedName() + fileExtension;

        try {
            Files.createDirectories(IMAGE_DIRECTORY);
            Path filePath = IMAGE_DIRECTORY.resolve(normalizedFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            product.setProductImage(normalizedFileName);
            return ProductResponseDTO.fromEntity(productRepository.save(product));

        } catch (IOException e) {
            throw new RuntimeException("Error uploading image", e);
        }
    }

    @Override
    public Resource getProductImage(UUID categoryId, UUID productId) {
        Product product = productRepository.findProductByUuid(productId);
        Path imagePath = IMAGE_DIRECTORY.resolve(product.getProductImage());

        try {
            if (!Files.exists(imagePath)) {
                throw new NoSuchElementException("Image not found: " + product.getProductImage());
            }
            return new UrlResource(imagePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Invalid file path for image: " + product.getProductImage(), e);
        }
    }

    @Override
    public ProductResponseDTO getProductByIdInCategory(UUID categoryId, UUID productId) {
        Category category = categoryRepository.findCategoryByUuid(categoryId);
        Product product = productRepository.findProductByUuid(productId);
        return ProductResponseDTO.fromEntity(product);
    }

    @Override
    @Transactional
    public void deleteProductByIdInCategory(UUID categoryId, UUID productId) {
        Category category = categoryRepository.findCategoryByUuid(categoryId);
        Product product = productRepository.findProductByUuid(productId);
        if (product.getProductImage() != null) {
            Path imagePath = IMAGE_DIRECTORY.resolve(product.getProductImage());
            try {
                Files.deleteIfExists(imagePath); // Deletes image if it exists
            } catch (IOException e) {
                throw new RuntimeException("Error deleting product image: " + product.getProductImage(), e);
            }
        }

        // Delete the product from the database
        productRepository.delete(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProductInCategory(UUID categoryId, UUID productId, ProductRequestDTO productRequest) {
        Category category = categoryRepository.findCategoryByUuid(categoryId);
        Product existingProduct = productRepository.findProductByUuid(productId);
        // Check if the new name already exists (excluding the current product)
        if (!existingProduct.getName().equalsIgnoreCase(productRequest.name()) &&
                productRepository.existsByName(productRequest.name().toLowerCase())) {
            throw new IllegalArgumentException("Product with this name already exists");
        }

        // Apply updates using DTO method
        existingProduct = productRequest.applyUpdatesTo(existingProduct, category);

        // Save the updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // Return response DTO
        return ProductResponseDTO.fromEntity(updatedProduct);
    }
}