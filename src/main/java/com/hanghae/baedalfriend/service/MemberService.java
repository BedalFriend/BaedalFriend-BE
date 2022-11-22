package com.hanghae.baedalfriend.service;
import com.hanghae.baedalfriend.dto.responsedto.EmailAuthResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.NicknameAuthResponseDto;
import com.hanghae.baedalfriend.dto.requestdto.*;
import com.hanghae.baedalfriend.dto.responsedto.MemberResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.MemberRepository;
import com.hanghae.baedalfriend.repository.PostRepository;
import com.hanghae.baedalfriend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PostRepository postRepository;
    // 회원가입 전 닉네임 인증 체크
    @Transactional
    public ResponseDto<?> nickname(@Valid NicknameAuthRequestDto requestDto) {
        if(null != isPresentNickname(requestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME","중복된 닉네임이에요!");
        }
        if (requestDto.getNickname().equals("")) {
            return ResponseDto.success("닉네임을 입력해주세요.");
        }
        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .build();
        return ResponseDto.success(
                    NicknameAuthResponseDto.builder()
                        .nickname(member.getNickname())
                        .build()
        );
    }

    // 닉네임 인증
    @Transactional
    public Object isPresentNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    // 이메일 인증
    @Transactional
    public ResponseDto<?> emailAuth(EmailAuthRequestDto requestDto) {
        //이메일 중복 체크
        if (null != isPresentMember(requestDto.getEmail())) {
            return ResponseDto.fail("DUPLICATED_EMAIL",
                    "중복된 이메일이에요!");
        }

        if(!requestDto.getEmail().contains("@")) {
            return ResponseDto.fail("INVALID_EMAIL",
                    "이메일 형식이 아니에요!");
        }

        if (requestDto.getEmail().equals("")) {
            return ResponseDto.success("이메일을 입력해주세요.");
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .build();
        return ResponseDto.success(
                EmailAuthResponseDto.builder()
                        .email(member.getEmail())
                        .build()
        );
    }

    // 회원가입
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) throws IOException {

        //이메일 중복 체크
        if (null != isPresentMember(requestDto.getEmail())) {
            return ResponseDto.fail("DUPLICATED_EMAIL",
                    "중복된 이메일 입니다.");
        }

        // 이메일 형식 체크
        if(!requestDto.getEmail().contains("@")) {
            return ResponseDto.fail("INVALID_EMAIL",
                    "이메일 형식이 아니에요!");
        }

        // 닉네임 중복 체크
        if(null != isPresentNickname(requestDto.getNickname())) {
            return ResponseDto.fail("INVALID_NICKNAME",
                    "중복된 닉네임 입니다.");
        }

        //패스워드 일치 체크
        if(!Objects.equals(requestDto.getPasswordConfirm(), requestDto.getPassword())){
            return ResponseDto.fail("PASSWORDCONFIRM_FAIL",
                    "패스워드가 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .address(requestDto.getAddress())
                .profileURL(requestDto.getProfileURL())
                .role(requestDto.getRole())
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .email(member.getEmail())
                        .address(member.getAddress())
                        .profileURL(member.getProfileURL())
                        .role(member.getRole())
                        .build()
        );
    }

    // 로그인
    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getEmail());

        // null값 사용자 유효성 체크
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        // 비밀번호 사용자 유효성 체크
        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        //해당 유저가 진행중인 게시글(공구) id가 포함되는지
        long roomId=0;
        if(postRepository.findByMember(member).size()==0){
            System.out.println("공구x");
        }else{
            roomId=postRepository.findByMember(member).get(0).getId();
        }
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .address(member.getAddress())
                        .email(member.getEmail())
                        .role(member.getRole())
                        .profileURL(member.getProfileURL())
                        .onGoing(roomId)
                        .build()
        );
    }

    // 회원 이메일 유효성 인증
    @Transactional
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    // 로그아웃
    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = (Member) tokenProvider.getMemberFromAuthentication();

        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        return tokenProvider.deleteRefreshToken(member);
    }

    // 헤더에 담기는 토큰
    private void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh_Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    // 리이슈
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        //해당 유저가 진행중인 게시글(공구) id가 포함되는지
       long roomId=0;

        Member member = refreshTokenRepository.findByValue(request.getHeader("Refresh_Token")).get().getMember();
        if(postRepository.findByMember(member).size()==0){
            System.out.println("공구x");
        }else{
            roomId=postRepository.findByMember(member).get(0).getId();
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .address(member.getAddress())
                        .modifiedAt(member.getModifiedAt())
                        .profileURL(member.getProfileURL())
                        .onGoing(roomId)
                        .role(member.getRole())
                        .email(member.getEmail())
                        .build()
        );
    }
}