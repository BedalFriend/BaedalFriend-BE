package com.hanghae.baedalfriend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Post extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member; // 회원

    @Column(nullable = false)
    private String target; // 식당 주소
    private String gather; // 집결지 주소

//    @Column(nullable = false)
//    private String category; // 카테고리

    @Column
    private boolean isDone; // 모집중

    private String imageUrl; // s3 이미지 관련 2022- 11- 11 (금) merge 예정

    @Column(nullable = false)
    private String category; // 카테고리

    @Column
    private int maxCapacity; // 최대수용인원

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private long limitTime; // 파티모집 마감시간

    @Column(nullable = false)
    private String content; // 게시글 내용
    private String nickname; // nickname 추가
    private String roomTitle; // 채팅방제목

    public void update(PostRequestDto postRequestDto) {
        this.content = postRequestDto.getContent(); // 게시글 내용
        this.roomTitle = postRequestDto.getRoomTitle(); // 채팅방제목
        this.target = postRequestDto.getTarget(); // 식당주소
        this.gather = postRequestDto.getGather(); // 집결지주소
        this.maxCapacity = postRequestDto.getMaxCapacity(); // 최대수용인원
        this.category = postRequestDto.getCategory(); // 카테고리
    }

    // 멤버 유효성 체크
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}