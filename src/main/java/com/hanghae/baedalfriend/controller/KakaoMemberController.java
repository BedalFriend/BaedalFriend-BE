package com.hanghae.baedalfriend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.hanghae.baedalfriend.dto.responsedto.MemberResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.service.KakaoMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/members/kakao")
public class KakaoMemberController {
    private final KakaoMemberService kakaoMemberService;

    @PostMapping("/callback")
    public ResponseDto<MemberResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        log.info("카카오콜백 {}" + code  );
        return kakaoMemberService.kakaoLogin(code, response);
    }
}
