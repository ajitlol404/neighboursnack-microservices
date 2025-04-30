package com.neighboursnack.productservice.service;

import com.neighboursnack.productservice.dto.ProductDTO.ProductRequestDTO;
import com.neighboursnack.productservice.dto.ProductDTO.ProductResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductResponseDTO> getAllProductsByCategoryId(UUID categoryId);

    ProductResponseDTO createProductInCategory(UUID categoryId, ProductRequestDTO productRequest);

    ProductResponseDTO uploadProductImage(UUID categoryId, UUID productId, MultipartFile file);

    Resource getProductImage(UUID categoryId, UUID productId);

    ProductResponseDTO getProductByIdInCategory(UUID categoryId, UUID productId);

    void deleteProductByIdInCategory(UUID categoryId, UUID productId);

    ProductResponseDTO updateProductInCategory(UUID categoryId, UUID productId, ProductRequestDTO productRequest);

}
