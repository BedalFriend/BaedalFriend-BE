package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.PopularSearchWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopularSearchWordRepository extends JpaRepository<PopularSearchWord, Long> {
    boolean existsBySearchWord(String searchWord);

    List<PopularSearchWord> findAllByOrderBySearchWordHitsDesc();
    PopularSearchWord findBySearchWord(String searchWord);

}
