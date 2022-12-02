package com.hanghae.baedalfriend.domain;


import com.hanghae.baedalfriend.domain.converter.ReviewCategory;
import com.hanghae.baedalfriend.domain.converter.ReviewCategoryConverter;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private int star;//받은 별점

    private Long reviewer;//리뷰남긴 사람 memberId

    private String isUnique;

    @Convert(converter = ReviewCategoryConverter.class)
    private ReviewCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 회원




}
