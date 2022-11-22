package com.hanghae.baedalfriend.controller;

import com.hanghae.baedalfriend.dto.requestdto.EmailAuthRequestDto;
import com.hanghae.baedalfriend.dto.requestdto.LoginRequestDto;
import com.hanghae.baedalfriend.dto.requestdto.MemberRequestDto;
import com.hanghae.baedalfriend.dto.requestdto.NicknameAuthRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/members")
public class MemberController {
    private final MemberService memberService;
    // 이메일 인증
    @PostMapping(value="/email")
    public ResponseDto<?> email(@RequestBody @Valid EmailAuthRequestDto requestDto) {
        return memberService.emailAuth(requestDto);
    }

    // 닉네임 인증
    @PostMapping(value = "/nickname")
    public ResponseDto<?> nickname(@RequestBody @Valid NicknameAuthRequestDto requestDto) {
        return memberService.nickname(requestDto);
    }

    //회원가입
    @PostMapping(value="/signup")
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) throws IOException {
        return memberService.createMember(requestDto);
    }

    //로그인
    @PostMapping(value = "/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    //로그아웃
    @PostMapping(value = "/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }

    //토큰재발급
    @PostMapping(value = "/reissue")
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return memberService.reissue(request, response);
    }
}