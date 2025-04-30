package com.neighboursnack.categoryservice.repository;

import com.neighboursnack.categoryservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByName(String name);

    Optional<Category> findByName(String name);

    default Category findCategoryByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new NoSuchElementException(
                        "Category with [NAME= " + name + "] not found"));
    }

    default Category findCategoryByUuid(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Category with [UUID= " + uuid + "] not found"));
    }

    List<Category> findByIsActiveTrue();

}