package com.hanghae.baedalfriend.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllPostResponseDto {
    private Long postId; // 게시글 아이디
    private Long memberId; // 회원 번호
    private String roomTitle; // 채팅방 제목
    private boolean isDone; // 모집중
    private Long maxCapacity; // 최대인원
    private String category; // 카테고리
    private String targetAddress; // 식당 주소
    private String targetName; // 식당이름
    private String region; // 지역
    private Long targetAmount; // 목표금액
    private String deliveryTime; // 배달시간
    private Long deliveryFee; // 배달요금
    private Long participantNumber; // 참여자수
    private String gatherName; // 모이는 장소 이름
    private String gatherAddress; // 모이는 장소 주소
    private Long hits; // 조회수
    private String nickname; // 닉네임
    private String profileURL; // 프로필사진
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 게시글 생성시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt; // 게시글 수정시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime limitTime;  // 마감시각
    private List<ChatRoomMember> chatRoomMembers;
}
