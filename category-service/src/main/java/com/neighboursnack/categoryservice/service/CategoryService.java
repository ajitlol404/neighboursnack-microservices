package com.neighboursnack.categoryservice.service;

import com.neighboursnack.categoryservice.dto.CategoryDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryDTO.CategoryRequestDTO categoryRequestDTO);

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryByUuid(UUID uuid);

    CategoryResponseDTO updateCategoryByUuid(UUID uuid, CategoryDTO.CategoryRequestDTO categoryRequestDTO);

    void deleteCategoryByUuid(UUID uuid);

    List<CategoryPublicResponseDTO> getActiveCategories();

}