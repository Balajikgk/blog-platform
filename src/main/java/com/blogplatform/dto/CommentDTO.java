package com.blogplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    @NotBlank(message = "Comment content is required")
    @Size(min = 3, max = 1000, message = "Comment must be between 3 and 1000 characters")
    private String content;

    private String username;
    private String userEmail;
    private LocalDateTime createdAt;
    private Long postId;
}
