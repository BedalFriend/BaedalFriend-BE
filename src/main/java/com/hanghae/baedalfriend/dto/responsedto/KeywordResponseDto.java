package com.hanghae.baedalfriend.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordResponseDto {
    private String keyword;
    private Long memberId;
}
