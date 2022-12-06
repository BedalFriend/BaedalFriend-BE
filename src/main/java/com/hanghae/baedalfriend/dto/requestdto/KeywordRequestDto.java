package com.hanghae.baedalfriend.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordRequestDto {
    @Id
    private Long id;
    @NotBlank(message = "검색어를 입력해주세요.")
    private String keyword;
}
