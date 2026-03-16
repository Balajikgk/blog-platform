package com.blogplatform.repository;

import com.blogplatform.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAll(Pageable pageable);

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.author JOIN FETCH p.category WHERE p.id = :id")
    Optional<Post> findByIdWithDetails(@Param("id") Long id);

    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    List<Post> findByCategoryIdOrderByCreatedAtDesc(Long categoryId);
}
