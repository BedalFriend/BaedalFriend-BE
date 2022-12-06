package com.hanghae.baedalfriend.service;

import com.hanghae.baedalfriend.chat.repository.ChatMessageJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberJpaRepository;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.domain.UserDetailsImpl;
import com.hanghae.baedalfriend.dto.requestdto.MypageRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.MypageResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.PostResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.repository.EventRepository;
import com.hanghae.baedalfriend.repository.MemberRepository;
import com.hanghae.baedalfriend.repository.PostRepository;
import com.hanghae.baedalfriend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    // 이미지 + 닉네임 변경
    @Transactional
    public ResponseDto<?> editMember(Long memberId, MypageRequestDto requestDto, MultipartFile multipartFile,
                                     UserDetailsImpl userDetails) throws IOException {
        Member member = findMember(memberId, userDetails);
        String profileURL = member.getProfileURL();
        //닉네임
        if (requestDto != null) {
            String nickname = requestDto.getNickname();
            if (nickname == null) {
                member.setNickname(member.getNickname());
            } else {
                member.setNickname(nickname);
            }
        }

        //프로필 이미지
        if (profileURL == null) { // 등록된 이미지가 없을 때 (기본이미지)
            if (multipartFile != null) { // 입력한 이미지 파일이 있을 때
                profileURL = s3Service.upload(multipartFile);
                member.setProfileURL(profileURL);
            }else {
                member.setProfileURL(profileURL);
            }
        }else { // 등록된 이미지가 있을 때 (url)
            if(multipartFile != null){
                s3Service.deleteImage(profileURL);
                profileURL = s3Service.upload(multipartFile);
                member.setProfileURL(profileURL);
            }else {//null로 들어올 때  1.등록된 이미지 파일이 있으면 이미지를 그대로 보낸다  2. 등록된 이미지가 있을 때 기본이미지로 보낸다\
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

    //주소 수정
    @Transactional
    public ResponseDto<?> updateAddress(Long memberId, MypageRequestDto requestDto, UserDetailsImpl userDetails) {
        Member member = findMember(memberId, userDetails);

        if(requestDto != null) { //주소가 들어올 때
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

        Post post = postRepository.findByMemberId(memberId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
        }
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId()) //게시글 아이디
                        .memberId(post.getMember().getId()) // 게시글 ID
                        .content(post.getContent()) // 게시글 내용
                        .roomTitle(post.getRoomTitle()) // 채팅방 제목
                        .region(post.getRegion()) // 지역
                        .isDone(post.isDone())// 모집중
                        .category(post.getCategory()) //카테고리
                        .maxCapacity(post.getMaxCapacity()) // 최대인원
                        .targetAddress(post.getTargetAddress()) // 식당주소
                        .targetName(post.getTargetName())// 식당이름
                        .targetAmount(post.getTargetAmount())// 목표금액
                        .deliveryTime(post.getDeliveryTime()) // 배달시간
                        .deliveryFee(post.getDeliveryFee()) // 배달요금
                        .participantNumber(post.getParticipantNumber()) // 참여자수
                        .gatherName(post.getGatherName()) // 모이는 장소 이름
                        .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                        .hits(post.getHits()) // 조회수
                        .createdAt(post.getCreatedAt()) // 생성일
                        .modifiedAt(post.getModifiedAt()) // 수정일
                        .nickname(post.getMember().getNickname()) // 작성자 닉네임
                        .profileURL(post.getMember().getProfileURL()) // 작성자 프로필 사진
                        .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                        .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                        .build()
        );
    }

    //회원 탈퇴
    @Transactional
    public ResponseDto<?> withdrawMember(Long memberId, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getMember().getId()).orElseThrow(
                () -> new IllegalArgumentException("등록되지 않은 회원입니다.")
        );
        Post post = postRepository.findByMemberId(memberId);

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
        memberRepository.deleteById(memberId);

        return ResponseDto.success("회원 탈퇴 완료");
    }

    @Transactional
    public Member findMember(Long memberId, UserDetailsImpl userDetails) {
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