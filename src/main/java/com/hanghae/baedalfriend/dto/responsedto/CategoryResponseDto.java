package com.hanghae.baedalfriend.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private Long exposureNumber;

    @Builder
    public CategoryResponseDto(Long categoryId, String categoryName, Long exposureNumber){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.exposureNumber = exposureNumber;
    }

}
