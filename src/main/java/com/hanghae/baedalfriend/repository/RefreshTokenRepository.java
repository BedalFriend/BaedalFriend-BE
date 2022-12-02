package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);

    Optional<RefreshToken> findByValue(String value);

    void deleteByMemberId(Long memberId);
}
