package com.neighboursnack.categoryservice.controller;

import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryRequestDTO;
import com.neighboursnack.categoryservice.dto.CategoryDTO.CategoryResponseDTO;
import com.neighboursnack.categoryservice.service.CategoryService;
import com.neighboursnack.common.dto.CategoryFilterRequest;
import com.neighboursnack.common.dto.PaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static com.neighboursnack.common.util.AppConstant.ADMIN_BASE_API_PATH;

@RestController
@RequestMapping(ADMIN_BASE_API_PATH + "/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryRequestDTO requestDTO) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(requestDTO);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{uuid}")
                        .buildAndExpand(createdCategory.uuid())
                        .toUri()
        ).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<CategoryResponseDTO>> getAllCategories(@Valid @ModelAttribute CategoryFilterRequest categoryFilterRequest) {
        return ResponseEntity.ok(categoryService.getAllCategories(categoryFilterRequest));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CategoryResponseDTO> getCategoryByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(categoryService.getCategoryByUuid(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable UUID uuid,
                                                              @RequestBody @Valid CategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(categoryService.updateCategoryByUuid(uuid, requestDTO));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID uuid) {
        categoryService.deleteCategoryByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryPublicResponseDTO>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

}
