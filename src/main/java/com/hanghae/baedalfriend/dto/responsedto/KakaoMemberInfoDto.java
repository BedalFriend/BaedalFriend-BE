package com.hanghae.baedalfriend.dto.responsedto;

import lombok.Getter;

@Getter
public class KakaoMemberInfoDto {
    private final String nickname;
    private final String email;
    private final String profileURL;
    private final Long id;

    public KakaoMemberInfoDto(String nickname, String email, String profileURL, Long id){
        this.nickname=nickname;
        this.email=email;
        this.profileURL=profileURL;
        this.id=id;
    }
}
