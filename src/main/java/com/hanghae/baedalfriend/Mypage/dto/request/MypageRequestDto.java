package com.hanghae.baedalfriend.Mypage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MypageRequestDto {

    private String nickname;
    private String address;
    private String profileURL;
}