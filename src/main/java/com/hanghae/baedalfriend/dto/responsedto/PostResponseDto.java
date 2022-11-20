package com.hanghae.baedalfriend.dto.responsedto;

import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    // s3 업로드 내용 2022-11-11 (금) 머지 예정
    private Long postId;
    private Long memberId;

    private String category; // 카테고리
    private String nickname; // 게시글 작성자 닉네임
    private String content; // 게시글 내용
    private String gather; // 집결지 주소
    private int maxCapacity; // 최대 수용 인원
    //    private String category; // 카테고리
    private String target; // 식당
    //    private int limitTime;
    private String roomTitle; // 채팅방 제목
    private String imageUrl; // 이미지url

    private boolean isDone; // 모집중
    private List<ChatRoomMember> chatRoomMembers;
    //  private Long limitTime; // 게시글의 파티원 모집 기간
    //  private LocalDateTime limitTime; //파티참여 마감시각

    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createdAt; // 게시글 생성시간
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime modifiedAt; // 게시글 수정시간

}