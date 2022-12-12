package com.hanghae.baedalfriend.domain.converter;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReviewCategory {
    GOOD1("시간 약속을 잘 지켜요"),
    GOOD2("친절해요"),
    GOOD3("답장이 빨라요"),
    GOOD4("거래매너가 좋아요"),
    BAD1("시간 약속을 안 지켜요"),
    BAD2("채팅 매너가 부족해요"),
    BAD3("연락이 잘 안돼요"),
    BAD4("거래 매너가 부족해요");




    private String value;

    ReviewCategory(String value) {
        this.value = value;
    };

    public static ReviewCategory fromCode(String dbData){
        return Arrays.stream(ReviewCategory.values())
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당카테고리가 존재하지않습니다",dbData)));
    };

}
