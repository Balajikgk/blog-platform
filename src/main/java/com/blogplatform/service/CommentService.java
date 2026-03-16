package com.blogplatform.service;

import com.blogplatform.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(Long postId, CommentDTO commentDTO);

    Page<CommentDTO> getCommentsByPost(Long postId, Pageable pageable);

    CommentDTO updateComment(Long id, CommentDTO commentDTO);

    void deleteComment(Long id);
}
