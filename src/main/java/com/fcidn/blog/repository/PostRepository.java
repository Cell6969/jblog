package com.fcidn.blog.repository;

import com.fcidn.blog.entity.Category;
import com.fcidn.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    Optional<Post> findBySlug(String slug);

    @Query(value = "SELECT * FROM posts WHERE slug = :slug AND (:isDeleted IS NULL OR is_deleted = :isDeleted) LIMIT 1", nativeQuery = true)
    Optional<Post> findFirstBySlugAndIsDeleted(String slug, Boolean isDeleted);
    Optional<Post> findFirstByIdAndIsDeleted(Integer id, boolean isDeleted);

    @Query("SELECT p FROM Post p WHERE (:isDeleted IS NULL OR p.isDeleted = :isDeleted) AND (:isPublished IS NULL OR p.isPublished = :isPublished)")
    Page<Post> findAllByIsDeletedAndIsPublished(Boolean isDeleted, Boolean isPublished ,Pageable pageable);

    Long countByCategory(Category category);
}
