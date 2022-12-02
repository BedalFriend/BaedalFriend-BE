package com.hanghae.baedalfriend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class RecentSearchTerms extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;
    private String roomTitle; // 추가
    @Column(nullable = false)
    private String searchWord; // 검색어
    public RecentSearchTerms(){}
    // 최근 검색
    public RecentSearchTerms(String searchWord) {
        this.memberId = memberId;
        this.searchWord = searchWord;
    }

    // 수정된 시간
//    public void updateTime(LocalDateTime now) {
//        this.setModifiedAt(now);
//    }

    public void setModifiedAt(LocalDateTime now) {
        this.setModifiedAt(now);
    }
}