package com.hanghae.baedalfriend.domain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Post extends Timestamped implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 회원
    @Column(nullable = false)
    private String roomTitle; // 채팅방제목
    @Column(nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private boolean isDone = false; // 모집중인지
    @Column(nullable = false)
    private String category; // 카테고리
    @Column(nullable = false)
    private String targetName; // 식당이름
    @Column(nullable = false)
    private String targetAddress; //식당주소
    @Column(nullable = false)
    private Long targetAmount; // 목표금액
    @Column(nullable = false)
    private String deliveryTime; //  배달시간

    @Column
    @ColumnDefault("0")
    private Long deliveryFee; //배달요금
    @Column(nullable = false)
    private String gatherName; // 모이는 장소 이름
    @Column(nullable = false)
    private String gatherAddress; // 모이는 장소 주소
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime limitTime; // 파티모집 마감시각


    @Column(nullable = false)
    private Long maxCapacity; // 최대인원

    @Column(nullable = false)
    private String region; // 지역

    private String keyword; // 검색어

    private String nickname; // 닉네임

    private String profileURL; //프로필 이미지 URL
    @Column
    @ColumnDefault("0")
    @Builder.Default
    private Long hits = 0L; // 조회수

    @Column
    @ColumnDefault("0")
    @Builder.Default
    private Long participantNumber = 0L; // 참여자수

    @Column
    @ColumnDefault("false")
    @Builder.Default
    private boolean isClosed = false;

    // 수정가능한 부분
    public void update(PostRequestDto postRequestDto) {
        this.roomTitle = postRequestDto.getRoomTitle(); // 채팅방제목
        this.category = postRequestDto.getCategory(); // 카테고리
        this.targetName = postRequestDto.getTargetName(); // 식당이름
        this.region = postRequestDto.getRegion(); // 지역
        this.targetAddress = postRequestDto.getTargetAddress(); // 식당주소
        this.targetAmount = postRequestDto.getTargetAmount(); // 목표금액
        this.deliveryTime = postRequestDto.getDeliveryTime(); // 배달시간
        this.participantNumber = postRequestDto.getParticipantNumber(); // 참여자수
        this.deliveryFee = postRequestDto.getDeliveryFee(); // 배달요금
        this.gatherName = postRequestDto.getGatherName(); // 모이는 장소 이름
        this.gatherAddress = postRequestDto.getGatherAddress(); // 모이는 장소 주소
        this.limitTime = postRequestDto.getLimitTime(); // 배달프렌드모집 마감시각
    }

    // 멤버 유효성 체크
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    // 배달프렌드 모집중
    public void isDone(boolean isDone) {
        this.isDone = isDone;
    }

    // 조회수
    public void hitsPost() {
        this.hits +=1L;
    }

    // 참가자수 증가
    public void updateParticipantNumber() {
        this.participantNumber +=1L;
    }

    // 참가자수 감소
    public void decreaseParticipantNumber() {
        this.participantNumber -=1L;
    }

    //공구종료
    public void isClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }
}