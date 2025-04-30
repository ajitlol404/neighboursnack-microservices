package com.neighboursnack.categoryservice.dto;

import com.neighboursnack.categoryservice.entity.Category;
import com.neighboursnack.common.util.AppUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;
import java.util.UUID;

public class CategoryDTO {

    public record CategoryRequestDTO(
            @NotBlank(message = "Category name is required")
            @Size(min = 3, max = 100, message = "Category name must be between 3 and 100 characters")
            @Pattern(regexp = "^[A-Za-z0-9\\-'/()& ]+$", message = "Category name can only contain letters, numbers, hyphens (-), apostrophes ('), slashes (/), parentheses (()), and ampersands (&).")
            String name,
            @Size(max = 500, message = "Category description must not exceed 500 characters") String description,
            @NotNull(message = "Category status is required") boolean isActive) {
        public Category toEntity() {
            return Category.builder()
                    .name(name.toLowerCase())
                    .normalizedName(AppUtil.normalizeName(name))
                    .description(description)
                    .isActive(isActive)
                    .build();
        }

        public Category updateCategory(Category category) {
            category.setName(name.toLowerCase());
            category.setNormalizedName(AppUtil.normalizeName(name));
            category.setDescription(description);
            category.setActive(isActive);
            return category;
        }
    }

    public record CategoryResponseDTO(UUID uuid, String name, String description, String normalizedName,
                                      boolean isActive, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        public static CategoryResponseDTO fromEntity(Category category) {
            return new CategoryResponseDTO(
                    category.getUuid(),
                    category.getName(),
                    category.getDescription(),
                    category.getNormalizedName(),
                    category.isActive(),
                    category.getCreatedAt(),
                    category.getUpdatedAt());
        }
    }

    public record CategoryPublicResponseDTO(UUID uuid, String name, String normalizedName) {
        public static CategoryPublicResponseDTO fromEntity(Category category) {
            return new CategoryPublicResponseDTO(
                    category.getUuid(),
                    category.getName(),
                    category.getNormalizedName());
        }
    }

}
