package com.hanghae.baedalfriend.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private Long memberId;
    private String roomTitle; // 채팅방 제목
    private boolean isDone; // 모집중
    private String category; // 카테고리
    private String targetAddress; // 식당 주소
    private String targetName; // 식당이름
    private Long targetAmount; // 목표금액
    private String deliveryTime; // 배달시간
    private Long deliveryFee; // 배달요금
    private String maxCapacity; // 최대인원
    private int participantNumber; // 참여자수
    private String gatherName; // 모이는 장소 이름
    private String gatherAddress; // 모이는 장소 주소
    private Long hits; // 조회수

    private String region; //지역
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 게시글 생성시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt; // 게시글 수정시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime limitTime;  // 마감시각
    private List<ChatRoomMember> chatRoomMembers;
    public Page<PostResponseDto> toDtoList(Page<Post> postList) {
        Page<PostResponseDto> ResponsePostList = postList.map(m -> PostResponseDto.builder()
                .postId(m.getId())
                .memberId(m.getId())
                .roomTitle(m.getRoomTitle())
                .category(m.getCategory())
                .targetAddress(m.getTargetAddress())
                .targetName(m.getTargetName())
                .targetAmount(m.getTargetAmount())
                .deliveryTime(m.getDeliveryTime())
                .deliveryFee(m.getDeliveryFee())
                .participantNumber(m.getParticipantNumber())
                .gatherName(m.getGatherName())
                .gatherAddress(m.getGatherAddress())
                .hits(m.getHits())
                .createdAt(m.getCreatedAt())
                .modifiedAt(m.getModifiedAt())
                .limitTime(m.getLimitTime())
                .build()
        );
        for(PostResponseDto postResponseDto : ResponsePostList){
            System.out.println("확인 : " + postResponseDto.gatherName);
            System.out.println("확인 : " + postResponseDto.roomTitle);
            System.out.println("확인 : " + postResponseDto.targetName);
        }
        return ResponsePostList;
    }
}