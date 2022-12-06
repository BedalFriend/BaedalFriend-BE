package com.hanghae.baedalfriend.dto.requestdto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "이메일과 비밀번호를 모두 입력해 주세요!")
    private String email;
    @NotBlank(message = "이메일과 비밀번호를 모두 입력해 주세요!")
    private String password;
}