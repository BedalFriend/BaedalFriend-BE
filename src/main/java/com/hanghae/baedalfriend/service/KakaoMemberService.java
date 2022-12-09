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
        String accessToken = getAccessToken(code); // "인가 코드"로 "액세스 토큰" 요청
        KakaoMemberInfoDto kakaoMemberInfo = getKakaoMemberInfo(accessToken); // 카카오 사용자 정보
        String nickname = String.valueOf(kakaoMemberInfo.getNickname());  // DB 에 중복된 Kakao Id 가 있는지 확인
        Member kakaoMember = memberRepository.findByKakaoId(kakaoMemberInfo.getId()).orElse(null);

        if (kakaoMember == null) {
            // 회원가입
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String profileURL = kakaoMemberInfo.getProfileURL();
            String email = kakaoMemberInfo.getEmail();
            Long kakaoId = kakaoMemberInfo.getId();
            kakaoMember = new Member(encodedPassword, profileURL, nickname, kakaoId, email);
            memberRepository.save(kakaoMember);

        } else {
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String profileURL = kakaoMemberInfo.getProfileURL();
            String email = kakaoMemberInfo.getEmail();
            Long kakaoId = kakaoMemberInfo.getId();
            kakaoMember = new Member(encodedPassword, profileURL, nickname, kakaoId, email);
            memberRepository.save(kakaoMember);
        }

        //강제 로그인 처리
        Member member = check.getMemberById(String.valueOf(kakaoMember.getId()));
        TokenDto tokenDto = tokenProvider.generateTokenDto(kakaoMember);
        tokenDto.tokenToHeaders(response);

        //해당 유저가 진행중인 게시글에 참여하고 있는지 확인
        long roomId = 0;
        List<ChatRoomMember> chatRoomMemberList = chatRoomMemberJpaRepository.findByMember(member);

        if (chatRoomMemberJpaRepository.findByMember(member).size() == 0) {

        } else {
            for (int i = 0; i < ((List<?>) chatRoomMemberList).size(); i++) {
                if ((!chatRoomMemberJpaRepository.findByMember(member).get(i).getChatRoom().getPost().isClosed())) {
                    roomId = chatRoomMemberJpaRepository.findByMember(member).get(i).getChatRoom().getId();
                    break;
                }
            }
        }

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(kakaoMember.getId())
                        .profileURL(kakaoMember.getProfileURL())
                        .createdAt(kakaoMember.getCreatedAt())
                        .modifiedAt(kakaoMember.getModifiedAt())
                        .email(kakaoMember.getEmail())
                        .address(kakaoMember.getAddress())
                        .nickname(kakaoMember.getNickname())
                        .onGoing(roomId)
                        .role(kakaoMember.getRole())
                        .build()
        );
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders(); // HTTP Header 생성
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(); // HTTP Body 생성
        body.add("grant_type", "authorization_code");
        body.add("client_id", myKaKaoRestAplKey);
        body.add("redirect_uri", "https://www.baedalfriend.app/kakaoLogin");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers); // HTTP 요청
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody(); // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders(); // HTTP Header 생성
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoMemberInfoRequest = new HttpEntity<>(headers); // HTTP 요청
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoMemberInfoRequest, String.class);
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong(); // 카카오 아이디
        String nickname = jsonNode.get("kakao_account").get("profile").get("nickname").asText(); // 닉네임
        String email = jsonNode.get("kakao_account").get("email").asText(); // 이메일
        String profilePhoto = jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText(); // 프로필 사진

        return new KakaoMemberInfoDto(nickname, profilePhoto, email, id);
    }
}