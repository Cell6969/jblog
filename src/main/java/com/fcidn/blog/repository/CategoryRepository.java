package com.fcidn.blog.repository;

import com.fcidn.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE (:isDeleted IS NULL OR c.isDeleted = :isDeleted)")
    Page<Category> findAllByIsDeletedOptional(Boolean isDeleted, PageRequest pageRequest);
}
