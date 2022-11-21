package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.RecentSearchTerms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecentSearchTermsRepository extends JpaRepository<RecentSearchTerms, Long> {
    boolean existsByIdAndSearchWord(Long id,String searchWord);

    RecentSearchTerms findByIdAndSearchWord(Long id, String searchWord);
    List<RecentSearchTerms> findAllByIdOrderByModifiedAtDesc(Long id);

    boolean findByRoomTitle(String keyword);
}
