package com.hanghae.baedalfriend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberJpaRepository;
import com.hanghae.baedalfriend.domain.Check;

import com.hanghae.baedalfriend.domain.Member;

import com.hanghae.baedalfriend.dto.requestdto.TokenDto;
import com.hanghae.baedalfriend.dto.responsedto.KakaoMemberInfoDto;
import com.hanghae.baedalfriend.dto.responsedto.MemberResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.MemberRepository;
import com.hanghae.baedalfriend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    private final Check check;
    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;

    @Value("${myKaKaoRestAplKey}")
    private String myKaKaoRestAplKey;

    @Transactional
    public ResponseDto<MemberResponseDto> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "?????? ??????"??? "????????? ??????" ??????
        String accessToken = getAccessToken(code);

        // 2. "????????? ??????"?????? "????????? ????????? ??????" ????????????
        KakaoMemberInfoDto kakaoMemberInfo = getKakaoMemberInfo(accessToken);

        // DB ??? ????????? Kakao Id ??? ????????? ??????
        String nickname = String.valueOf(kakaoMemberInfo.getNickname());

        Member kakaoMember = memberRepository.findByKakaoId(kakaoMemberInfo.getId())
                .orElse(null);
        log.info("kakaoMember : {} " ,kakaoMember);
        System.out.println(" ===================kakaoMember================================================");


        if (kakaoMember == null) {
            // ????????????
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String profileURL = kakaoMemberInfo.getProfileURL();
            Long kakaoId= kakaoMemberInfo.getId();
            kakaoMember = new Member(encodedPassword, profileURL, nickname, kakaoId);




            memberRepository.save(kakaoMember);

        }

        // 4. ?????? ????????? ??????
//        System.out.println("=============================?????? ????????? ??????============================================");
        Member member = check.getMemberById(String.valueOf(kakaoMember.getId()));
        TokenDto tokenDto = tokenProvider.generateTokenDto(kakaoMember);
        tokenDto.tokenToHeaders(response);



        //?????? ????????? ???????????? ???????????? ???????????? ????????? ???????????? ??????
        long roomId=0;
        List<ChatRoomMember> chatRoomMemberList=chatRoomMemberJpaRepository.findByMember(member);
        if(chatRoomMemberJpaRepository.findByMember(member).size()==0){

        }else{
            for (int i = 0; i < ((List<?>) chatRoomMemberList).size() ; i++) {
                if((!chatRoomMemberJpaRepository.findByMember(member).get(i).getChatRoom().getPost().isClosed())){
                    roomId=chatRoomMemberJpaRepository.findByMember(member).get(i).getChatRoom().getId();
                    break;

                }
            }

        }


        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(kakaoMember.getId())
//                        .nickname(kakaoMember.getNickname())
                        .profileURL(kakaoMember.getProfileURL())
                        .createdAt(kakaoMember.getCreatedAt())
                        .modifiedAt(kakaoMember.getModifiedAt())
                        .nickname(kakaoMember.getNickname()) // ????????? ????????? ?????? 2022- 12 -02
                        .onGoing(roomId)
                        .role(kakaoMember.getRole())
                        .build()
        );
    }

    private String getAccessToken(String code) throws JsonProcessingException {

        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", myKaKaoRestAplKey);
        body.add("redirect_uri", "https://www.baedalfriend.app/kakaoLogin");
        body.add("code", code);

        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP ?????? (JSON) -> ????????? ?????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoMemberInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoMemberInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        System.out.println(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("kakao_account").get("profile")
                .get("nickname").asText();

        String profilePhoto = jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText();

        System.out.println("????????? ????????? ??????: " + id + ", " + nickname);

        return new KakaoMemberInfoDto(nickname,  profilePhoto, id);
    }
}