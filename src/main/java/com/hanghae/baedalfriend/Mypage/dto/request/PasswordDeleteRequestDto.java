package com.hanghae.baedalfriend.Mypage.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class PasswordDeleteRequestDto {
    @NotNull
    @Size(min=4,max=32, message= "비밀번호는 최소 4자이상 최대 32자미만으로 만들어주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{4,32}$"
            , message = "비밀번호에 영어대소문자, 숫자, 특수문자를 모두 포함해주세요")
    private String password;

}
