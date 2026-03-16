package com.blogplatform.repository;

import com.blogplatform.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPosts();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.category.id = :categoryId")
    long countPostsByCategoryId(@Param("categoryId") Long categoryId);
}
