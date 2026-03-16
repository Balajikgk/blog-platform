package com.blogplatform.controller;

import com.blogplatform.dto.CommentDTO;
import com.blogplatform.service.CommentService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comment management APIs")
@SecurityRequirement(name = "BearerAuth")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    @Operation(summary = "Create a new comment", description = "Creates a new comment on a post")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(
            @Parameter(description = "Post ID") @PathVariable Long postId,
            @Valid @RequestBody CommentDTO commentDTO) {
        
        CommentDTO response = commentService.createComment(postId, commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CommentDTO>builder()
                        .success(true)
                        .message("Comment created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated comments for a post", description = "Retrieves paginated comments for a specific post")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<ApiResponse<Page<CommentDTO>>> getCommentsByPostPaginated(
            @Parameter(description = "Post ID") @PathVariable Long postId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<CommentDTO> response = commentService.getCommentsByPost(postId, pageable);
        return ResponseEntity.ok()
                .body(ApiResponse.<Page<CommentDTO>>builder()
                        .success(true)
                        .message("Comments retrieved successfully")
                        .data(response)
                        .build());
    }

    @PutMapping("/update/{commentId}")
    @Operation(summary = "Update comment", description = "Updates an existing comment")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to update this comment")
    })
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @Parameter(description = "Post ID") @PathVariable Long postId,
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Valid @RequestBody CommentDTO commentDTO) {
        
        CommentDTO response = commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok()
                .body(ApiResponse.<CommentDTO>builder()
                        .success(true)
                        .message("Comment updated successfully")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "Delete comment", description = "Deletes a comment")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to delete this comment")
    })
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @Parameter(description = "Post ID") @PathVariable Long postId,
            @Parameter(description = "Comment ID") @PathVariable Long commentId) {
        
        commentService.deleteComment(commentId);
        return ResponseEntity.ok()
                .body(ApiResponse.<Void>builder()
                        .success(true)
                        .message("Comment deleted successfully")
                        .build());
    }
}
