package com.hanghae.baedalfriend.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private Long id;
    private String nickname; // 게시글 작성자 닉네임
    private String content; // 게시글 내용
    private String target; // 식당 주소
    private Long limitTime; // 마감시각

    private String gather; // 집결지 주소

    private String category; // 카테고리

    private String imageUrl; // 2022-11-10 s3 업로드 merge 예정
    private int maxCapacity; // 최대수용인원
    private String roomTitle; // 채팅방 제목

}