package com.hanghae.baedalfriend.service;

import com.hanghae.baedalfriend.domain.Keyword;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.dto.requestdto.KeywordRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Component
@RequiredArgsConstructor
public class KeywordService {
    private final TokenProvider tokenProvider;
    private final KeywordRepository keywordRepository;

    @Transactional
    public ResponseDto<?> saveKeyword(@Valid KeywordRequestDto keywordRequestDto, HttpServletRequest request) throws Exception {
        // Refresh_Token 유효성 검사
        if (null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // Authorization 유효성 검사
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회원 유효성 검사
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 검색어 공백 검사
        if (keywordRequestDto.getKeyword().isBlank()) {
            return ResponseDto.fail("KEYWORD_IS_BLANK", "검색어를 입력해주세요.");
        }

        Keyword keyword = Keyword.builder()
                .keyword(keywordRequestDto.getKeyword())
                .member(member)
                .build();
        keywordRepository.save(keyword);
        return ResponseDto.success("검색어가 저장되었습니다.");

    }

    // 회원 유효성 검사
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    // 유저가 등록한 검색어 조회
    @Transactional
    public ResponseDto getMyKeyword(HttpServletRequest request) {
        // Refresh_Token 유효성 검사
        if (null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }
        // Authorization 유효성 검사
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }
        // 회원 유효성 검사
        Member member = validateMember(request);

        //최근 등록순 7개 조회
        List<Keyword> keywordList2 = keywordRepository.findAllByMemberIdOrderByCreatedAtDesc(member.getId()).stream().limit(7).toList();

        //LinkedHashMap 사용해서 중복 제거
        HashMap<String, Keyword> keywordMap = new LinkedHashMap<>();
        for (Keyword keyword : keywordList2) {
            keywordMap.put(keyword.getKeyword(), keyword);
        }

        List<Keyword> keywordList = new ArrayList<>(keywordMap.values());

        return ResponseDto.success(keywordList);

    }

    // 검색어 삭제
    @Transactional
    public ResponseDto deleteKeyword(Long keywordId, HttpServletRequest request) {
        // Refresh_Token 유효성 검사
        if(null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // Authorizatio 유효성 검사
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회원 유효성 검사
        Member member = validateMember(request);

        // 유저가 등록한 최근 검색어
        List<Keyword> keywordList = keywordRepository.findAllByMemberIdOrderByCreatedAtDesc(member.getId());
        for(Keyword keyword : keywordList) {
            if(keyword.getId().equals(keywordId)) {
                keywordRepository.deleteById(keywordId);
                return ResponseDto.success("검색어가 삭제되었습니다.");
            }
        }
        return ResponseDto.fail("KEYWORD_NOT_FOUND","검색어가 존재하지 않습니다.");
    }
}