package com.hanghae.baedalfriend.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventUpRequestDto {
    private String title;
    private String content;
    private String imageUrl;
}
