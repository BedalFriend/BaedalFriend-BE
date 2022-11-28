package com.hanghae.baedalfriend.domain;

import com.hanghae.baedalfriend.dto.PhotoDto;
import com.hanghae.baedalfriend.dto.requestdto.EventRequestDto;
import com.hanghae.baedalfriend.dto.requestdto.EventUpRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //제목
    @Column(nullable = false)
    private String title;

    //내용
    @Column(nullable = false)
    private String content;

    @Column
    private String imageUrl;
    //멤버(관리자)
    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    public void update(EventUpRequestDto requestDto, List<PhotoDto> photoDtos) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.imageUrl = photoDtos.get(0).getPath();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }


}

