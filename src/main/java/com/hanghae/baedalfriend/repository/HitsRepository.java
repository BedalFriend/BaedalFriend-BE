package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.shared.Hits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HitsRepository extends JpaRepository<Hits, Long> {
     boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
