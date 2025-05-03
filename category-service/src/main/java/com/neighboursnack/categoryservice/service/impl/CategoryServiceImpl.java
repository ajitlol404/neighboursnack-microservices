package com.neighboursnack.categoryservice.service.impl;

import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryRequestDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryResponseDTO;
import com.neighboursnack.categoryservice.entity.Category;
import com.neighboursnack.categoryservice.repository.CategoryRepository;
import com.neighboursnack.categoryservice.service.CategoryService;
import com.neighboursnack.common.dto.CategoryFilterRequest;
import com.neighboursnack.common.dto.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public PaginationResponse<CategoryResponseDTO> getAllCategories(CategoryFilterRequest categoryFilterRequest) {
//        return categoryRepository.findAll().stream()
//                .map(CategoryResponseDTO::fromEntity)
//                .toList();


        // Sorting logic
        Sort sort = categoryFilterRequest.sortDir().equalsIgnoreCase("asc")
                ? Sort.by(categoryFilterRequest.sortBy()).ascending()
                : Sort.by(categoryFilterRequest.sortBy()).descending();

        // Pageable for pagination and sorting
        Pageable pageable = PageRequest.of(categoryFilterRequest.page(), categoryFilterRequest.size(), sort);

        // Specification for filtering
        Specification<Category> spec = Specification.where(null);

        // Filter by name (if provided)
        if (categoryFilterRequest.name() != null && !categoryFilterRequest.name().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + categoryFilterRequest.name().toLowerCase() + "%"));
        }

        // Filter by isActive (if provided)
        if (categoryFilterRequest.isActive() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("isActive"), categoryFilterRequest.isActive()));
        }

        // Fetch filtered data with pagination and sorting
        Page<Category> categoryPage = categoryRepository.findAll(spec, pageable);

        // Map entities to response DTOs
        List<CategoryResponseDTO> content = categoryPage.getContent()
                .stream()
                .map(CategoryResponseDTO::fromEntity)
                .toList();

        return new PaginationResponse<>(
                content,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast(),
                categoryFilterRequest.sortBy(),
                categoryFilterRequest.sortDir()
        );

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
