package com.hanghae.baedalfriend.dto.responsedto;

import com.hanghae.baedalfriend.domain.RecentSearchTerms;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecentSearchTermsResponseDto {
    private String searchWord;
    private LocalDateTime searchTime;
    @Builder
    public RecentSearchTermsResponseDto(String searchWord, LocalDateTime searchTime){
        this.searchWord = searchWord;
        this.searchTime = searchTime;
    }
}
