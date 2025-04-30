package com.neighboursnack.categoryservice.service.impl;

import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryRequestDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryResponseDTO;
import com.neighboursnack.categoryservice.entity.Category;
import com.neighboursnack.categoryservice.repository.CategoryRepository;
import com.neighboursnack.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO createCategoryDTO) {

        // Check if category already exists
        if (categoryRepository.existsByName(createCategoryDTO.name().toLowerCase())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        // Convert DTO to entity, set normalizedName, and save
        Category category = createCategoryDTO.toEntity();
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponseDTO.fromEntity(savedCategory);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public CategoryResponseDTO getCategoryByUuid(UUID uuid) {
        Category category = categoryRepository.findCategoryByUuid(uuid);
        return CategoryResponseDTO.fromEntity(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategoryByUuid(UUID uuid, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = categoryRepository.findCategoryByUuid(uuid);

        // Check if the name is being updated
        if (!existingCategory.getName().equalsIgnoreCase(categoryRequestDTO.name()) &&
                categoryRepository.existsByName(categoryRequestDTO.name().toLowerCase())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        // Update and save the category in one step
        Category savedCategory = categoryRepository.save(categoryRequestDTO.updateCategory(existingCategory));

        return CategoryResponseDTO.fromEntity(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryByUuid(UUID uuid) {
        Category category = categoryRepository.findCategoryByUuid(uuid);
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryPublicResponseDTO> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(CategoryPublicResponseDTO::fromEntity)
                .toList();
    }

}
