package com.hanghae.baedalfriend.domain;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "hits", nullable = false)
    @ColumnDefault("0")
    private Long hits;

    public Category(){}

    @Builder
    public Category(String category, Long hits){
        this.category = category;
        this.hits = hits;
    }

    public void updateCateHits(String cate){
        this.category = cate;
        this.hits += 1;
    }
}