package com.blogplatform.controller;

import com.blogplatform.Utils.ApiResponse;
import com.blogplatform.dto.PostRequestDTO;
import com.blogplatform.dto.PostResponseDTO;
import com.blogplatform.security.JwtTokenProvider;
import com.blogplatform.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private PostController postController;

    private PostResponseDTO responseDTO;

    @Before
    public void setUp() {

        responseDTO = new PostResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Test Post");
        responseDTO.setContent("Test Content");
    }

    @Test
    public void testCreatePost() {

        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("Test Post");

        when(postService.createPost(Mockito.any())).thenReturn(responseDTO);

        ResponseEntity<ApiResponse<PostResponseDTO>> response =
                postController.createPost(requestDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    public void testGetAllPosts() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("createdAt").descending());

        Page<PostResponseDTO> page =
                new PageImpl<>(Arrays.asList(responseDTO));

        when(postService.getAllPosts(Mockito.any())).thenReturn(page);

        ResponseEntity<ApiResponse<Page<PostResponseDTO>>> response =
                postController.getAllPosts(0,10,"createdAt","desc");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testGetPostById() {

        when(postService.getPostById(1L)).thenReturn(responseDTO);

        ResponseEntity<ApiResponse<PostResponseDTO>> response =
                postController.getPostById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Post", response.getBody().getData().getTitle());
    }

    @Test
    public void testUpdatePost() {

        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("Updated Post");

        when(postService.updatePost(Mockito.eq(1L), Mockito.any()))
                .thenReturn(responseDTO);

        ResponseEntity<ApiResponse<PostResponseDTO>> response =
                postController.updatePost(1L, requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    public void testDeletePost() {

        Mockito.doNothing().when(postService).deletePost(1L);

        ResponseEntity<ApiResponse<Void>> response =
                postController.deletePost(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    public void testSearchPosts() {

        List<PostResponseDTO> list = Arrays.asList(responseDTO);

        when(postService.searchPosts("test")).thenReturn(list);

        ResponseEntity<ApiResponse<List<PostResponseDTO>>> response =
                postController.searchPosts("test");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getData().size());
    }
}