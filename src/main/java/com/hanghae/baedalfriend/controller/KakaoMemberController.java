package com.hanghae.baedalfriend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.hanghae.baedalfriend.dto.responsedto.MemberResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.service.KakaoMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class KakaoMemberController {
    private final KakaoMemberService kakaoMemberService;

    @GetMapping("/kakao/callback")
    public ResponseDto<MemberResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoMemberService.kakaoLogin(code, response);
    }
}
