package com.neighboursnack.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.util.Optional;

public record CategoryFilterRequest(
        @Min(value = 0, message = "Page number must be 0 or greater")
        Integer page,
        @Min(value = 1, message = "Page size must be at least 1")
        Integer size,
        String sortBy,
        @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Sort direction must be 'asc' or 'desc'")
        String sortDir,
        String name,
        Boolean isActive
) {
    public CategoryFilterRequest {
        page = Optional.ofNullable(page).orElse(0);
        size = Optional.ofNullable(size).orElse(10);
        sortBy = Optional.ofNullable(sortBy).orElse("createdAt");
        sortDir = Optional.ofNullable(sortDir).orElse("desc");
    }
}
