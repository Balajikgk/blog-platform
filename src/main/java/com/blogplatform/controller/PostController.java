package com.blogplatform.controller;

import com.blogplatform.dto.PostRequestDTO;
import com.blogplatform.dto.PostResponseDTO;
import com.blogplatform.security.JwtTokenProvider;
import com.blogplatform.service.PostService;
import com.blogplatform.Utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Post management APIs")
@SecurityRequirement(name = "BearerAuth")
public class PostController {

    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    @Operation(summary = "Create a new post", description = "Creates a new blog post")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Post created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(
            @Valid @RequestBody PostRequestDTO postRequestDTO
            ) {
        
        PostResponseDTO response = postService.createPost(postRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PostResponseDTO>builder()
                        .success(true)
                        .message("Post created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get all posts", description = "Retrieves all blog posts with pagination")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<PostResponseDTO>>> getAllPosts(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<PostResponseDTO> response = postService.getAllPosts(pageable);
        return ResponseEntity.ok()
                .body(ApiResponse.<Page<PostResponseDTO>>builder()
                        .success(true)
                        .message("Posts retrieved successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieves a specific blog post by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<ApiResponse<PostResponseDTO>> getPostById(
            @Parameter(description = "Post ID") @PathVariable Long id) {
        
        PostResponseDTO response = postService.getPostById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.<PostResponseDTO>builder()
                        .success(true)
                        .message("Post retrieved successfully")
                        .data(response)
                        .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Updates an existing blog post")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to update this post")
    })
    public ResponseEntity<ApiResponse<PostResponseDTO>> updatePost(
            @Parameter(description = "Post ID") @PathVariable Long id,
            @Valid @RequestBody PostRequestDTO postRequestDTO) {
        
        PostResponseDTO response = postService.updatePost(id, postRequestDTO);
        return ResponseEntity.ok()
                .body(ApiResponse.<PostResponseDTO>builder()
                        .success(true)
                        .message("Post updated successfully")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Deletes a blog post")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to delete this post")
    })
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @Parameter(description = "Post ID") @PathVariable Long id) {

        postService.deletePost(id);

        return ResponseEntity.ok()
                .body(ApiResponse.<Void>builder()
                        .success(true)
                        .message("Post deleted successfully")
                        .build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search posts", description = "Search posts by keyword")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> searchPosts(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        
        List<PostResponseDTO> response = postService.searchPosts(keyword);
        return ResponseEntity.ok()
                .body(ApiResponse.<List<PostResponseDTO>>builder()
                        .success(true)
                        .message("Search completed successfully")
                        .data(response)
                        .build());
    }
}
