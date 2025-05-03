package com.neighboursnack.categoryservice.service;

import com.neighboursnack.categoryservice.dto.CategoryDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryResponseDTO;
import com.neighboursnack.common.dto.CategoryFilterRequest;
import com.neighboursnack.common.dto.PaginationResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryDTO.CategoryRequestDTO categoryRequestDTO);

    PaginationResponse<CategoryResponseDTO> getAllCategories(CategoryFilterRequest categoryFilterRequest);

    CategoryResponseDTO getCategoryByUuid(UUID uuid);

    CategoryResponseDTO updateCategoryByUuid(UUID uuid, CategoryDTO.CategoryRequestDTO categoryRequestDTO);

    void deleteCategoryByUuid(UUID uuid);

    List<CategoryPublicResponseDTO> getActiveCategories();

}