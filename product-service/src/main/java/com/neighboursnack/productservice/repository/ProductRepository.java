package com.neighboursnack.productservice.repository;

import com.neighboursnack.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsByName(String name);

    boolean existsByNormalizedName(String normalizedName);

    default Product findProductByUuid(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Product with [UUID= " + uuid + "] not found"));
    }

}