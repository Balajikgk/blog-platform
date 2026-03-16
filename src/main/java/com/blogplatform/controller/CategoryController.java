package com.blogplatform.controller;

import com.blogplatform.dto.CategoryDTO;
import com.blogplatform.service.CategoryService;
import com.blogplatform.Utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management APIs")
@SecurityRequirement(name = "BearerAuth")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new blog category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Category created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO response = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CategoryDTO>builder()
                        .success(true)
                        .message("Category created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves all blog categories")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> response = categoryService.getAllCategories();
        return ResponseEntity.ok()
                .body(ApiResponse.<List<CategoryDTO>>builder()
                        .success(true)
                        .message("Categories retrieved successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable Long id) {
        
        CategoryDTO response = categoryService.getCategoryById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.<CategoryDTO>builder()
                        .success(true)
                        .message("Category retrieved successfully")
                        .data(response)
                        .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @Parameter(description = "Category ID") @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        
        CategoryDTO response = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok()
                .body(ApiResponse.<CategoryDTO>builder()
                        .success(true)
                        .message("Category updated successfully")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Deletes a category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Category has associated posts")
    })
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable Long id) {
        
        categoryService.deleteCategory(id);
        return ResponseEntity.ok()
                .body(ApiResponse.<Void>builder()
                        .success(true)
                        .message("Category deleted successfully")
                        .build());
    }
}
