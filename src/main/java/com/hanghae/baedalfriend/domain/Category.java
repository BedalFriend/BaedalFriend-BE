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

    @Column(name = "category",nullable = false)
    private String category;

    @Column(name = "viewerCnt",nullable = false)
    @ColumnDefault("0")
    private Long exposureNumber;

    public Category(){}

    @Builder
    public Category(String category, Long exposureNumber){
        this.category = category;
        this.exposureNumber = exposureNumber;
    }

    public void updateExposureNumber(String cate){
        this.category = cate;
        this.exposureNumber += 1;
    }
}