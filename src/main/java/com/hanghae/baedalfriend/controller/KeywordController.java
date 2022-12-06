package com.hanghae.baedalfriend.controller;

import com.hanghae.baedalfriend.dto.requestdto.KeywordRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class KeywordController {
    private final KeywordService keywordService;

    // 검색어 저장
    @PostMapping("/posts/keyword/create")
    public ResponseDto saveKeyword(@Valid @RequestBody KeywordRequestDto keywordRequestDto, HttpServletRequest request) throws Exception {
        return keywordService.saveKeyword(keywordRequestDto, request);
    }

    //내가 검색한 최근 검색어 조회
    @GetMapping("/posts/keyword/my")
    public ResponseDto getMyKeyword(HttpServletRequest request) throws Exception {
        return keywordService.getMyKeyword(request);
    }

    //최근 검색어 삭제
    @DeleteMapping("/posts/keyword/delete")
    public ResponseDto deleteKeyword(@RequestParam Long keywordId, HttpServletRequest request) throws Exception {
        return keywordService.deleteKeyword(keywordId, request);
    }
}