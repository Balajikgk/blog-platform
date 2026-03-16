package com.blogplatform.service.impl;

import com.blogplatform.dto.CommentDTO;
import com.blogplatform.entity.Comment;
import com.blogplatform.entity.Post;
import com.blogplatform.entity.User;
import com.blogplatform.exception.ResourceNotFoundException;
import com.blogplatform.mapper.CommentMapper;
import com.blogplatform.repository.CommentRepository;
import com.blogplatform.repository.PostRepository;
import com.blogplatform.repository.UserRepository;
import com.blogplatform.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDTO(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByPost(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        return comments.map(commentMapper::toDTO);
    }

    @Override
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        commentMapper.updateEntityFromDTO(commentDTO, existingComment);

        Comment updatedComment = commentRepository.save(existingComment);
        return commentMapper.toDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        commentRepository.delete(comment);
    }
}
