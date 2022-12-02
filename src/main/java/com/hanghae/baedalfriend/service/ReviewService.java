package com.hanghae.baedalfriend.service;


import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.domain.Review;
import com.hanghae.baedalfriend.dto.requestdto.ReviewRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.MemberRepository;
import com.hanghae.baedalfriend.repository.PostRepository;
import com.hanghae.baedalfriend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseDto<?> createReview(Long postId, List<ReviewRequestDto> reviewRequestDtoList,
                                       HttpServletRequest request) {

        Member member = validateMember(request);


        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("해당하는 게시글이 없습니다")
        );

        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }

        if (null == request.getHeader("Refresh_token")) {
            return ResponseDto.fail("INVALID_TOKEN",
                    "Token이 유효하지 않습니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("INVALID_TOKEN",
                    "Token이 유효하지 않습니다.");
        }

        for (ReviewRequestDto reviews : reviewRequestDtoList) {

                    Review review = Review.builder()
                    .reviewer(member.getId())
                    .star(reviews.getStar())
                    .category(reviews.getCategory())
                    .member(memberRepository.findById(reviews.getMemberId()).get())
                    .isUnique(String.valueOf(member.getId() + memberRepository.findById(reviews.getMemberId()).get().getId() + postId))
                    .build();
            reviewRepository.save(review);
        }

        return ResponseDto.success("임시");
    }


    @javax.transaction.Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
