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
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.domain.UserDetailsImpl;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.repository.EventRepository;
import com.hanghae.baedalfriend.repository.MemberRepository;
import com.hanghae.baedalfriend.repository.PostRepository;
import com.hanghae.baedalfriend.repository.RefreshTokenRepository;
import com.hanghae.baedalfriend.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final EventRepository eventRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;

    // 프로필 이미지 삭제
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
    // 이미지 + 닉네임 변경
    public ResponseDto<?> editMember(Long memberId, MypageRequestDto requestDto, MultipartFile multipartFile,
                                     UserDetailsImpl userDetails) throws IOException {
        Member member = findMember(memberId, userDetails);
        //닉네임

        if (requestDto != null) {
            String nickname = requestDto.getNickname();
            String nicknamePattern = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*${2,40}";
            if (nickname.equals("")) {
                return ResponseDto.fail("BAD_REQUEST", "닉네임을 입력해주세요.");
            } else if (memberRepository.findByNickname(nickname).isPresent()) {
                return ResponseDto.fail("BAD_REQUEST", "중복된 닉네임이 존재합니다.");
            } else if (2 > nickname.length() || 40 < nickname.length()) {
                return ResponseDto.fail("BAD_REQUEST", "닉네임은 2자 이상 40자 이하이어야 합니다.");
            } else if (!Pattern.matches(nicknamePattern, nickname)) {
                return ResponseDto.fail("BAD_REQUEST", "닉네임은 영문, 한글, 숫자만 가능합니다.");
            }
            member.setNickname(nickname);
        }
        member.setNickname(member.getNickname());

        //프로필 이미지
        String profileURL = member.getProfileURL();
        if (!multipartFile.isEmpty()) { // 입력한 이미지가 있을 때
            if(profileURL == null) { //등록된 이미지는 없을 때
                profileURL = s3Service.upload(multipartFile);
                member.setProfileURL(profileURL);
            } else { //등록된 이미지는 있을 때
                s3Service.deleteImage(profileURL);
                profileURL = s3Service.upload(multipartFile);
                member.setProfileURL(profileURL);
            }
        } else { //입력한 이미지가 없을 때
            if (profileURL != null) { //등록된 이미지도 있을 때
                s3Service.deleteImage(profileURL);
                profileURL = null;
            }
        }
        member.update(member.getNickname(), profileURL);
        memberRepository.save(member);

        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //주소 수정
    @Transactional
    public ResponseDto<?> updateAddress(Long memberId, MypageRequestDto requestDto, UserDetailsImpl userDetails) {
        Member member = findMember(memberId, userDetails);

        if(requestDto != null) { //주소가 들어올 때
            String address = requestDto.getAddress();
            if(address == null) {
                ResponseDto.fail("REGISTER_YOUR_ADDRESS","주소를 등록해주세요");
            } else {
                member.setAddress(requestDto.getAddress());
            }
        }

        memberRepository.save(member);

        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //유저 정보 조회
    @Transactional
    public ResponseDto<?> getMemberInfo(Long memberId, UserDetailsImpl userDetails) {
        Member member = findMember(memberId, userDetails);

        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //내가 쓴 글
    @Transactional
    public ResponseDto<?> getMyPost(Long memberId, UserDetailsImpl userDetails) {
        findMember(memberId, userDetails);

        Post Post = postRepository.findAllByMemberId(memberId);
        if (null == Post) {
            return ResponseDto.fail("POST_NOT_FOUND",
                    "게시글이 존재하지 않습니다.");
        }

        List<Post> postList = postRepository.findAllByMemberIdOrderByIdDesc(memberId);
        return ResponseDto.success(postList);
    }

    //내가 들어간 채팅방 (참여내역)
    @Transactional
    public ResponseDto<?> getMyChat(Long memberId, UserDetailsImpl userDetails) {
        findMember(memberId, userDetails);

        Post post = postRepository.findAllByMemberId(memberId);
        ChatRoom chatRoom = chatRoomJpaRepository.findAllByPost(post);
        List<ChatMessage> chatMessages = chatMessageJpaRepository.findAllByMemberId(memberId);

        if (post == null) { //채팅 참가자
            MypageChatResponseDto mypageChatResponseDto = MypageChatResponseDto.builder()
                    .chatRoomMembers(chatRoomMemberJpaRepository.findAllByMemberId(memberId).get(0).getChatRoom().getMemberList())
                    .chatMessages(chatMessages)
                    .build();
            return ResponseDto.success(mypageChatResponseDto);
        } else if (chatRoom != null) { //방장
            MypageChatResponseDto mypageChatResponseDto = MypageChatResponseDto.builder()
                    .chatRoomMembers(chatRoomMemberJpaRepository.findAllByMemberId(memberId).get(0).getChatRoom().getMemberList())
                    .chatMessages(chatMessages)
                    .build();
            return ResponseDto.success(mypageChatResponseDto);
        } else {
            return ResponseDto.fail("CHATROOM_NOT_FOUND", "채팅방이 존재하지 않습니다");
        }
    }

    //비밀번호 변경
    @Transactional
    public ResponseDto<?> updatePassword(PasswordRequestDto requestDto, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            return ResponseDto.fail("BAD_REQUEST", "비밀번호를 다시 확인해 주세요.");
        }
        String password = passwordEncoder.encode(requestDto.getNewPassword());

        member.updateUserPassword(password);
        memberRepository.save(member);

        return ResponseDto.success("비밀번호 변경 완료");
    }

    //회원 탈퇴
    @Transactional
    public ResponseDto<?> withdrawMember(Long memberId, PasswordDeleteRequestDto passwordDeleteRequestDto,
                                         UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getMember().getId()).orElseThrow(
                () -> new IllegalArgumentException("등록되지 않은 회원입니다.")
        );
        Post post = postRepository.findAllByMemberId(memberId);

        //hard Delete
        refreshTokenRepository.deleteByMemberId(memberId);
        chatRoomMemberJpaRepository.deleteByMemberId(memberId);
        chatRoomJpaRepository.deleteByPost(post);
        chatMessageJpaRepository.deleteByMemberId(memberId);
        postRepository.deleteByMemberId(memberId);
        eventRepository.deleteByMemberId(memberId);

        if (member.getProfileURL() != null) {
            String fileName = member.getProfileURL();
            s3Service.deleteImage(fileName);
        }

        if (passwordEncoder.matches(passwordDeleteRequestDto.getPassword(), member.getPassword())) {
            memberRepository.deleteById(memberId);
            SecurityContextHolder.clearContext();
        } else {
            return ResponseDto.fail("PASSWORD_NOT_MATCH", "패스워드가 일치하지 않습니다");
        }
        return ResponseDto.success("회원 탈퇴 완료");
    }

    private Member findMember(Long memberId, UserDetailsImpl userDetails) {
        //member 정보 찾기
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저 정보를 찾을 수 없습니다.")
        );
        //memberId와 로그인한 사용자Id가 다를 때
        if (!member.getId().equals(userDetails.getMember().getId())) {
            throw new IllegalArgumentException("해당 유저 정보를 찾을 수 없습니다.");
        }
        return member;
    }
}
