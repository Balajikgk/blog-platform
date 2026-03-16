package com.blogplatform.service.impl;

import com.blogplatform.dto.PostRequestDTO;
import com.blogplatform.dto.PostResponseDTO;
import com.blogplatform.entity.Category;
import com.blogplatform.entity.Post;
import com.blogplatform.entity.User;
import com.blogplatform.exception.ResourceNotFoundException;
import com.blogplatform.mapper.PostMapper;
import com.blogplatform.repository.CategoryRepository;
import com.blogplatform.repository.PostRepository;
import com.blogplatform.repository.UserRepository;
import com.blogplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        User author = userRepository.findByUsername(postRequestDTO.getUserName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + postRequestDTO.getUserName()));

        Post post = postMapper.toEntity(postRequestDTO);
        post.setAuthor(author);

        if (postRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(postRequestDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            post.setCategory(category);
        }

        Post savedPost = postRepository.save(post);
        return postMapper.toResponseDTO(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return postMapper.toResponseDTO(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(postMapper::toResponseDTO);
    }

    @Override
    public PostResponseDTO updatePost(Long id, PostRequestDTO postRequestDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (postRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(postRequestDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingPost.setCategory(category);
        }

        postMapper.updateEntityFromDTO(postRequestDTO, existingPost);

        Post updatedPost = postRepository.save(existingPost);
        return postMapper.toResponseDTO(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id.toString()));

        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDTO> searchPosts(String keyword) {
        List<Post> posts = postRepository.findByKeyword(keyword, Pageable.ofSize(50)).getContent();
        return postMapper.toResponseDTOList(posts);
    }

}
