package com.hanghae.baedalfriend.dto.requestdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class PostRequestDto {
    private Long id;

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String roomTitle; // 채팅방 제목

    private boolean isDone; // 모집중

    @NotBlank(message = "내용을 입력해주세요!")
    @Size(max=40, message= "내용은 최대 40자미만으로 만들어주세요.")
    private String content; // 내용

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String category; // 카테고리

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String targetAddress; // 식당 주소

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String targetName; // 식당이름

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private Long targetAmount; // 목표금액

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String deliveryTime; //  배달시간

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private Long maxCapacity; // 최대인원

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private Long deliveryFee; // 배달요금

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private Long participantNumber; // 참여자수

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String gatherName; // 모이는 장소 이름

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String gatherAddress; // 모이는 장소 주소

    private String nickname; // 닉네임
    private String profileURL; // 프로필 사진

    private Long hits; // 조회수
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime limitTime; // 마감시각

   private String region; //지역

    @QueryProjection
    public PostRequestDto( Long id, String roomTitle, boolean isDone, String content, String category, String targetAddress, String targetName, Long targetAmount, String deliveryTime, Long maxCapacity, Long deliveryFee, Long participantNumber, String gatherName, String gatherAddress, String nickname, String profileURL, Long hits, LocalDateTime limitTime, String region) {
        this.id = id;
        this.roomTitle = roomTitle;
        this.isDone = isDone;
        this.category = category;
        this.targetAddress = targetAddress;
        this.targetName = targetName;
        this.targetAmount = targetAmount;
        this.deliveryTime = deliveryTime;
        this.maxCapacity = maxCapacity;
        this.deliveryFee = deliveryFee;
        this.participantNumber = participantNumber;
        this.gatherName = gatherName;
        this.gatherAddress = gatherAddress;
        this.nickname = nickname;
        this.profileURL = profileURL;
        this.hits = hits;
        this.limitTime = limitTime;
        this.region = region;
    }

    public Long getPostId() {
        return id;
    }
}