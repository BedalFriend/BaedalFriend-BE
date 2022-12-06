package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findAllByMemberIdOrderByCreatedAtDesc(Long id);
}
