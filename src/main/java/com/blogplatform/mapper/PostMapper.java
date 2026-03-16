package com.blogplatform.mapper;

import com.blogplatform.dto.PostRequestDTO;
import com.blogplatform.dto.PostResponseDTO;
import com.blogplatform.entity.Category;
import com.blogplatform.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

   default Post toEntity(PostRequestDTO dto) {

        if (dto == null) {
            return new Post();
        }

        Post post = new Post();

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        // map category
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            post.setCategory(category);
        }

        return post;
    }

    default PostResponseDTO toResponseDTO(Post post) {

        if (post == null) {
            return null;
        }

        PostResponseDTO dto = new PostResponseDTO();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        if (post.getAuthor() != null) {
            dto.setAuthorUsername(post.getAuthor().getUsername());
            dto.setAuthorEmail(post.getAuthor().getEmail());
        }

        if (post.getCategory() != null) {
            dto.setCategoryId(post.getCategory().getId());
            dto.setCategoryName(post.getCategory().getName());
        }

        return dto;
    }

    default List<PostResponseDTO> toResponseDTOList(List<Post> posts) {

        if (posts == null) {
            return null;
        }

        return posts.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    default void updateEntityFromDTO(PostRequestDTO dto, @MappingTarget Post post) {

        if (dto == null || post == null) {
            return;
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            post.setCategory(category);
        }
    }
}