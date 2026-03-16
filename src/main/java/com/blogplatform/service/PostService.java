package com.blogplatform.service;

import com.blogplatform.dto.PostRequestDTO;
import com.blogplatform.dto.PostResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequestDTO);

    PostResponseDTO getPostById(Long id);

    Page<PostResponseDTO> getAllPosts(Pageable pageable);

    PostResponseDTO updatePost(Long id, PostRequestDTO postRequestDTO);

    void deletePost(Long id);

    List<PostResponseDTO> searchPosts(String keyword);


}
