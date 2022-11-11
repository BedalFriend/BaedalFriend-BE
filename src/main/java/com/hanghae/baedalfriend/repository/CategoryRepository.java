package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findAllByOrderByExposureNumberDesc();
    List<Category> findAllByOrderByCategoryAsc();

}
