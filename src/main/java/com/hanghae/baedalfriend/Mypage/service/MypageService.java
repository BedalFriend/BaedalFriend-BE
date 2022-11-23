package com.hanghae.baedalfriend.Mypage.service;

import com.hanghae.baedalfriend.Mypage.dto.request.MypageRequestDto;
import com.hanghae.baedalfriend.Mypage.dto.request.PasswordDeleteRequestDto;
import com.hanghae.baedalfriend.Mypage.dto.request.PasswordRequestDto;
import com.hanghae.baedalfriend.Mypage.dto.response.MypageChatResponseDto;
import com.hanghae.baedalfriend.Mypage.dto.response.MypageResponseDto;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.repository.ChatMessageJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberJpaRepository;
import com.hanghae.baedalfriend.domain.*;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.repository.MemberRepository;
import com.hanghae.baedalfriend.repository.PostRepository;
import com.hanghae.baedalfriend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
//    private final EventRepository eventRepository;
//    private final RefreshTokenRepository refreshTokenRepository;
    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;


    //닉네임 수정
    @Transactional
    public ResponseDto<?> updateMember(Long memberId, MypageRequestDto requestDto, UserDetailsImpl userDetails) {
        Member member = findMember(memberId, userDetails);
        String nickname = requestDto.getNickname();
        String nicknamePattern = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*${2,40}";

        //닉네임
        if(nickname.equals(member.getNickname())) {
            nickname = member.getNickname();
        } else {
            if(nickname.equals("")){
                throw new IllegalArgumentException("닉네임을 입력해주세요");
            } else if(memberRepository.findByNickname(nickname).isPresent()){
                throw new IllegalArgumentException("중복된 닉네임이 존재합니다");
            } else if( 2 > nickname.length() || 40 < nickname.length()) {
                throw new IllegalArgumentException("닉네임은 2자 이상 40자 이하이어야 합니다.");
            } else if(!Pattern.matches(nicknamePattern, nickname)) {
                throw new IllegalArgumentException("닉네임은 영문, 한글, 숫자만 가능합니다.");
            }
        }

        // user 프로필 업데이트
        member.setNickname(nickname);
        memberRepository.save(member); // DB에 저장

        //수정한 거 Dto에 저장해서 반환하기
        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //프로필 이미지 수정
    @Transactional
    public ResponseDto<?> updateImage(Long memberId, MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
        Member member = findMember(memberId, userDetails);

        if (multipartFile != null) {
            if(member.getProfileURL() != null) {
                s3Service.deleteImage(member.getProfileURL());
                String profileURL = s3Service.upload(multipartFile);
                member.setProfileURL(profileURL);
            } else {
                String profileURL = s3Service.upload(multipartFile);
                member.setProfileURL(profileURL);
            }
        }
        memberRepository.save(member); // DB에 저장

        //수정한 거 Dto에 저장해서 반환하기
        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //프로필 이미지 삭제
    @Transactional
    public ResponseDto<?> deleteProfileImage(Long memberId, UserDetailsImpl userDetails) {
        findMember(memberId, userDetails);
        Member member = userDetails.getMember();
        s3Service.deleteImage(member.getProfileURL());

        member.setProfileURL(null);
        memberRepository.save(member);

        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //유저 정보 조회
    @Transactional
    public ResponseDto<?> getMemberInfo(Long memberId, UserDetailsImpl userDetails) {
        Member member = findMember(memberId, userDetails);

        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //내가 쓴 글
    @Transactional
    public ResponseDto<?> getMyPost(Long memberId, UserDetailsImpl userDetails) {
        findMember(memberId, userDetails);

        List<Post> postList = postRepository.findAllByMemberIdOrderByIdDesc(memberId);
        return ResponseDto.success(postList);
    }

    //내가 들어간 채팅방 (참여내역)
    @Transactional
    public ResponseDto<?> getMyChat(Long memberId, UserDetailsImpl userDetails) {
        findMember(memberId, userDetails);

        Post post = postRepository.findAllByMemberId(memberId);
        List<ChatRoom> chatRoomList = chatRoomJpaRepository.findAllByPost(post);
        List<ChatMessage> chatMessages = chatMessageJpaRepository.findAllByMemberId(memberId);
        MypageChatResponseDto mypageChatResponseDto = MypageChatResponseDto.builder()
                .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //멤버
                .chatMessages(chatMessages)
                .chatRooms(chatRoomList) //방장
                .build();
        return ResponseDto.success(mypageChatResponseDto);
    }

    //비밀번호 변경
    @Transactional
    public ResponseDto<?> updatePassword(PasswordRequestDto requestDto, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
        }
        String password = passwordEncoder.encode(requestDto.getNewPassword());

        member.updateUserPassword(password);
        memberRepository.save(member);

        return ResponseDto.success("비밀번호 변경 완료");
    }

    //회원 탈퇴
    public ResponseDto<?> withdrawMember(Long memberId, PasswordDeleteRequestDto passwordDeleteRequestDto,
                                         UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getMember().getId()).orElseThrow(
                () -> new IllegalArgumentException("등록되지 않은 회원입니다.")
        );

        //hard Delete
//        refreshTokenRepository.deleteByMemberId(memberId);
//        postRepository.deleteByMemberId(memberId);
//        eventRepository.deleteByMemberId(memberId);
//        chatRoomMemberJpaRepository.deleteByMemberId(memberId);
//        chatMessageJpaRepository.deleteByMemberId(memberId);

        String defaultImg = "https://sprtbucket.s3.ap-northeast-2.amazonaws.com/image/default.jpg";
        if(!Objects.equals(member.getProfileURL(), defaultImg)){
            String fileName = member.getProfileURL();

            s3Service.deleteImage(fileName);
        }


        if (passwordEncoder.matches(passwordDeleteRequestDto.getPassword(), member.getPassword())) {
            memberRepository.deleteById(memberId);
            SecurityContextHolder.clearContext();
        } else {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        return ResponseDto.success("회원 탈퇴 완료");
    }

    private Member findMember(Long memberId, UserDetailsImpl userDetails) {
        //member 정보 찾기
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저 정보를 찾을 수 없습니다.")
        );
        //memberId와 로그인한 사용자Id가 다를 때
        if(!member.getId().equals(userDetails.getMember().getId())) {
            throw new IllegalArgumentException("해당 유저 정보를 찾을 수 없습니다.");
        }
        return member;
    }
}