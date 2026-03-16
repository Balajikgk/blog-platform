package com.blogplatform.mapper;

import com.blogplatform.dto.CommentDTO;
import com.blogplatform.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default Comment toEntity(CommentDTO dto) {

        if (dto == null) {
            return null;
        }

        Comment comment = new Comment();

        comment.setContent(dto.getContent());

        return comment;
    }

    default CommentDTO toDTO(Comment comment) {

        if (comment == null) {
            return null;
        }

        CommentDTO dto = new CommentDTO();

        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        if (comment.getUser() != null) {
            dto.setUsername(comment.getUser().getUsername());
            dto.setUserEmail(comment.getUser().getEmail());
        }

        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getId());
        }

        return dto;
    }

    default List<CommentDTO> toDTOList(List<Comment> comments) {

        if (comments == null) {
            return null;
        }

        return comments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    default void updateEntityFromDTO(CommentDTO dto, @MappingTarget Comment comment) {

        if (dto == null || comment == null) {
            return;
        }

        comment.setContent(dto.getContent());
    }
}