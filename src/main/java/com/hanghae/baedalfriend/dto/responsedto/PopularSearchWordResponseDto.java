package com.hanghae.baedalfriend.dto.responsedto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PopularSearchWordResponseDto {
    private String searchWord;
    private Long searchWordHits;
    @Builder
    public PopularSearchWordResponseDto(String searchWord, Long searchWordHits) {
        this.searchWord = searchWord;
        this.searchWordHits = searchWordHits;
    }
}