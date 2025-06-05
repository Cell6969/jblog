package com.fcidn.blog.repository;

import com.fcidn.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    Optional<Post> findBySlug(String slug);
    Optional<Post> findFirstBySlugAndIsDeleted(String slug, boolean isDeleted);
    Optional<Post> findFirstByIdAndIsDeleted(Integer id, boolean isDeleted);
    Page<Post> findAllByIsDeleted(boolean isDeleted, Pageable pageable);
}
