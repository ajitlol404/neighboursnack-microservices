package com.neighboursnack.categoryservice.service.impl;

import com.neighboursnack.categoryservice.entity.Category;
import com.neighboursnack.common.dto.CategoryFilterRequest;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecifications {

    public static Specification<Category> build(CategoryFilterRequest request) {
        Specification<Category> spec = Specification.where(null);

        if (request.name() != null && !request.name().isBlank()) {
            String likePattern = "%" + request.name().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), likePattern));
        }

        if (request.search() != null && !request.search().isBlank()) {
            String keyword = "%" + request.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("name")), keyword),
                            cb.like(cb.lower(root.get("description")), keyword)
                    ));
        }

        if (request.isActive() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("isActive"), request.isActive()));
        }

        if (request.createdFrom() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), request.createdFrom()));
        }

        if (request.createdTo() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), request.createdTo()));
        }

        return spec;
    }
}
