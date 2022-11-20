package com.hanghae.baedalfriend.shared;

import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@DynamicInsert
public class Hits {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private Long postId;
    public Hits(Long memberId, Long postId){
        this.memberId = memberId;
        this.postId = postId;
    }
    public Hits() {}
}
