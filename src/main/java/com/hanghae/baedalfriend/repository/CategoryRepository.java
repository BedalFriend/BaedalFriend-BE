package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByCategory(String category);
    Optional<Category> findByCategory(String category);
}
