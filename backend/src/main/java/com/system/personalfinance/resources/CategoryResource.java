package com.system.personalfinance.resources;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.system.personalfinance.dtos.category.*;
import com.system.personalfinance.entities.Category;
import com.system.personalfinance.security.UserPrincipal;
import com.system.personalfinance.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> list(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getId();

        List<CategoryResponse> result = categoryService.list(userId).stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getType(), c.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateCategoryRequest request) {

        Long userId = principal.getId();
        Category saved = categoryService.create(userId, request);

        CategoryResponse body = new CategoryResponse(saved.getId(), saved.getName(), saved.getType(), saved.getCreatedAt());
        URI location = URI.create("/api/categories/" + saved.getId());

        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        Long userId = principal.getId();
        Category c = categoryService.getById(userId, id);

        return ResponseEntity.ok(new CategoryResponse(c.getId(), c.getName(), c.getType(), c.getCreatedAt()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {

        Long userId = principal.getId();
        Category saved = categoryService.update(userId, id, request);

        return ResponseEntity.ok(new CategoryResponse(saved.getId(), saved.getName(), saved.getType(), saved.getCreatedAt()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        categoryService.delete(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
