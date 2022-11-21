package com.hanghae.baedalfriend.domain;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
public class PopularSearchWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String searchWord;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long searchWordHits = 1L;
    public PopularSearchWord(){}
    public PopularSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }
    public void updateSearchWordHits() {
        this.searchWordHits += 1;
    }
    public void updateCount() {
        this.searchWordHits += 1;
    }
}