package com.hanghae.baedalfriend.domain;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region",nullable = false)
    private String region;

    @Column(name = "hits",nullable = false)
    @ColumnDefault("0")
    private Long hits;

    public Region(){}

    public Region(String region, Long hits) {
        this.region = region;
        this.hits = hits;

    }

    public void updateRegionHits(String regi){
        this.region = regi;
        this.hits += 1;
    }

}
