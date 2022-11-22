package com.hanghae.baedalfriend.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthRequestDto {
    @NotBlank(message = "공백은 허용하지 않습니다.")
    @Size(min=8,max=30, message= "8자리이상 30자리 미만 글자로 email를 만들어주세요")
    @Pattern(regexp = "^[0-9a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+$" , message = "이메일 형식을 확인해 주세요.")
    private String email;
}
