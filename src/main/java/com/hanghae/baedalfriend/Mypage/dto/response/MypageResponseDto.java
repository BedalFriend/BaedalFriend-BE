package com.hanghae.baedalfriend.Mypage.dto.response;

import com.hanghae.baedalfriend.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MypageResponseDto {
    private Long mamberId;

    private String nickname;

    private String email;

    private String address;

    private String profileURL;

    public MypageResponseDto(Member member) {
        this.mamberId = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.profileURL = member.getProfileURL();
    }


}
