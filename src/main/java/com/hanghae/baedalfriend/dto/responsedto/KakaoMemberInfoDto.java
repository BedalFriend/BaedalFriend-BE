package com.hanghae.baedalfriend.dto.responsedto;
import lombok.Getter;

@Getter
public class KakaoMemberInfoDto {
    private final String nickname;
    private final String profileURL;
    private final String email;
    private final Long id;

    public KakaoMemberInfoDto(String nickname, String profileURL, String email, Long id) {
        this.nickname=nickname;
        this.profileURL=profileURL;
        this.email = email;
        this.id=id;
    }
}