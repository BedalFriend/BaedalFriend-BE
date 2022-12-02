package com.hanghae.baedalfriend.dto.requestdto;
import com.hanghae.baedalfriend.shared.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    @NotBlank(message = "이메일과 비밀번호를 모두 입력해주세요!")
    @Size(min=8,max=30, message= "8자리이상 30자리 미만 글자로 email를 만들어주세요")
    @Pattern(regexp = "^[0-9a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+$" , message = "이메일 형식을 확인해 주세요.")
    private String email;


    @NotBlank(message = "닉네임을 입력해주세요!")
    @Size(min=1,max=40, message= "닉네임은 최소 1자이상 최대 40자미만으로 만들어주세요.")
    @Pattern(regexp = "[0-9a-zA-Zㄱ-ㅎ가-힣]*${1,40}", message = "닉네임 형식을 확인해 주세요.")
    private String nickname;

    @NotBlank(message = "이메일과 비밀번호를 모두 입력해주세요!")
    @Size(min=4,max=32, message= "비밀번호는 최소 4자이상 최대 32자미만으로 만들어주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{4,32}$"
            , message = "비밀번호에 영어대소문자, 숫자, 특수문자를 모두 포함해주세요")
    private String password;

    private String profileURL;

    private Authority role;

    private String address;

    public String passwordConfirm;
}
