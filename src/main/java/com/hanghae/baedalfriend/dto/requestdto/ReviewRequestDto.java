package com.hanghae.baedalfriend.dto.requestdto;

import com.hanghae.baedalfriend.domain.converter.ReviewCategory;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ReviewRequestDto {
    private int star;
    private Long memberId;
    private ReviewCategory category;
}
