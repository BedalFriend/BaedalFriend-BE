package com.hanghae.baedalfriend.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllPostResponseDto {
    private Long id;
    private String roomTitle; // 채팅방 제목
    private String content; // 게시글 내용
    private String nickname; // 게시글 작성자 닉네임
    private String target; // 식당
    private String gather; // 집결지 주소
    private LocalDateTime createdAt; // 게시글 생성시간
    private LocalDateTime modifiedAt; // 게시글 수정시간
    private int maxCapacity; // 최대 수용 인원
    private String imageUrl; // imageUrl
  ///  private Long limitTime; // 게시글의 모집 마감시각
  //  private String category;
   public  String category;
}
