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
import java.util.Objects;

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

    // ????????? + ????????? ??????
    @Transactional
    public ResponseDto<?> editMember(Long memberId, MypageRequestDto requestDto, MultipartFile multipartFile,
                                     UserDetailsImpl userDetails) throws IOException {
        Member member = findMember(memberId, userDetails);
        String profileURL = member.getProfileURL();
        //?????????
        if (requestDto != null) {
            String nickname = requestDto.getNickname();
            if (nickname == null) {
                member.setNickname(member.getNickname());
            } else {
                member.setNickname(nickname);
            }
        }

        //????????? ?????????
        if (profileURL == null) { // ????????? ???????????? ?????? ??? (???????????????)
            if (multipartFile != null) { // ????????? ????????? ????????? ?????? ???
                profileURL = s3Service.upload(multipartFile);
                member.setProfileURL(profileURL);
            }else {
                member.setProfileURL(profileURL);
            }
        }else { // ????????? ???????????? ?????? ??? (url)
            if(multipartFile != null){
                s3Service.deleteImage(profileURL);
                profileURL = s3Service.upload(multipartFile);
                member.setProfileURL(profileURL);
            }else {//null??? ????????? ???  1.????????? ????????? ????????? ????????? ???????????? ????????? ?????????  2. ????????? ???????????? ?????? ??? ?????????????????? ?????????\
                if(requestDto != null) {
                    String profile = requestDto.getProfileURL();
                    if(Objects.equals(profile, "BasicProfile")) {
                        s3Service.deleteImage(profileURL);
                        profileURL = null;
                        member.setProfileURL(null);
                    }
                } else {
                        member.setProfileURL(profileURL);
                }
            }
        }

        member.update(member.getNickname(), profileURL);
        memberRepository.save(member);

        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //?????? ??????
    @Transactional
    public ResponseDto<?> updateAddress(Long memberId, MypageRequestDto requestDto, UserDetailsImpl userDetails) {
        Member member = findMember(memberId, userDetails);

        if(requestDto != null) { //????????? ????????? ???
            String address = requestDto.getAddress();
            if(address == null) {
                member.setAddress(member.getAddress());
            } else {
                member.setAddress(requestDto.getAddress());
            }
        }

        memberRepository.save(member);

        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //?????? ?????? ??????
    @Transactional
    public ResponseDto<?> getMemberInfo(Long memberId, UserDetailsImpl userDetails) {
        Member member = findMember(memberId, userDetails);

        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "???????????? ?????? ??? ????????????.");
        }

        MypageResponseDto mypageResponseDto = new MypageResponseDto(member);
        return ResponseDto.success(mypageResponseDto);
    }

    //?????? ??? ???
    @Transactional
    public ResponseDto<?> getMyPost(Long memberId, UserDetailsImpl userDetails) {
        findMember(memberId, userDetails);

        Post Post = postRepository.findAllByMemberId(memberId);
        if (null == Post) {
            return ResponseDto.fail("POST_NOT_FOUND",
                    "???????????? ???????????? ????????????.");
        }

        List<Post> postList = postRepository.findAllByMemberIdOrderByIdDesc(memberId);
        return ResponseDto.success(postList);
    }

    //?????? ????????? ????????? (????????????)
    @Transactional
    public ResponseDto<?> getMyChat(Long memberId, UserDetailsImpl userDetails) {
        findMember(memberId, userDetails);

        Post post = postRepository.findAllByMemberId(memberId);
        ChatRoom chatRoom = chatRoomJpaRepository.findAllByPost(post);
        List<ChatMessage> chatMessages = chatMessageJpaRepository.findAllByMemberId(memberId);

        if (post == null) { //?????? ?????????
            MypageChatResponseDto mypageChatResponseDto = MypageChatResponseDto.builder()
                    .chatRoomMembers(chatRoomMemberJpaRepository.findAllByMemberId(memberId).get(0).getChatRoom().getMemberList())
                    .chatMessages(chatMessages)
                    .build();
            return ResponseDto.success(mypageChatResponseDto);
        } else if (chatRoom != null) { //??????
            MypageChatResponseDto mypageChatResponseDto = MypageChatResponseDto.builder()
                    .chatRoomMembers(chatRoomMemberJpaRepository.findAllByMemberId(memberId).get(0).getChatRoom().getMemberList())
                    .chatMessages(chatMessages)
                    .build();
            return ResponseDto.success(mypageChatResponseDto);
        } else {
            return ResponseDto.fail("CHATROOM_NOT_FOUND", "???????????? ???????????? ????????????");
        }
    }

    //???????????? ??????
    @Transactional
    public ResponseDto<?> updatePassword(PasswordRequestDto requestDto, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            return ResponseDto.fail("BAD_REQUEST", "??????????????? ?????? ????????? ?????????.");
        }
        String password = passwordEncoder.encode(requestDto.getNewPassword());

        member.updateUserPassword(password);
        memberRepository.save(member);

        return ResponseDto.success("???????????? ?????? ??????");
    }

    //?????? ??????
    @Transactional
    public ResponseDto<?> withdrawMember(Long memberId, PasswordDeleteRequestDto passwordDeleteRequestDto,
                                         UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getMember().getId()).orElseThrow(
                () -> new IllegalArgumentException("???????????? ?????? ???????????????.")
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
            return ResponseDto.fail("PASSWORD_NOT_MATCH", "??????????????? ???????????? ????????????");
        }
        return ResponseDto.success("?????? ?????? ??????");
    }

    private Member findMember(Long memberId, UserDetailsImpl userDetails) {
        //member ?????? ??????
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("?????? ?????? ????????? ?????? ??? ????????????.")
        );
        //memberId??? ???????????? ?????????Id??? ?????? ???
        if (!member.getId().equals(userDetails.getMember().getId())) {
            throw new IllegalArgumentException("?????? ?????? ????????? ?????? ??? ????????????.");
        }
        return member;
    }
}