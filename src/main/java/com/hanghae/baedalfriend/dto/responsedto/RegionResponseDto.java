package com.hanghae.baedalfriend.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegionResponseDto {
    private Long regionId;
    private String regionName;
    private Long hits;

    @Builder
    public RegionResponseDto(Long regionId, String regionName, Long hits) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.hits = hits;
    }
}
