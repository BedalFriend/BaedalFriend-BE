package com.hanghae.baedalfriend.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDto {
    private Long eventId;
    private String title;
    private String content;
    private String imageUrl;
}
