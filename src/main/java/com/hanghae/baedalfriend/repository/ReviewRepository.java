package com.hanghae.baedalfriend.repository;


import com.hanghae.baedalfriend.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository  extends JpaRepository<Review,Long> {
}
