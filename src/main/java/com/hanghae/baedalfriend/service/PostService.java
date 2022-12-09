package com.hanghae.baedalfriend.service;

import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.repository.ChatRoomJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberJpaRepository;
import com.hanghae.baedalfriend.chat.service.ChatRoomService;
import com.hanghae.baedalfriend.domain.*;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.*;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.*;
import com.hanghae.baedalfriend.shared.Hits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final HitsRepository hitsRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final RegionRepository regionRepository;
    private final TokenProvider tokenProvider;
    private final ChatRoomService chatRoomService;
    private final ChatRoomJpaRepository chatRoomRepository;
    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;

    // 게시글 등록
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto,
                                     HttpServletRequest request) throws Exception {
        // Refresh_Token 유효성 검사
        if (null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // Authorization 유효성 검사
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // 회원 유효성 검사
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 카테고리
        if (categoryRepository.count() == 0) {
            Category category0 = new Category("패스트푸드", 0L);

            categoryRepository.save(category0);
            Category category1 = new Category("치킨", 0L);

            categoryRepository.save(category1);
            Category category2 = new Category("분식", 0L);

            categoryRepository.save(category2);
            Category category3 = new Category("야식", 0L);

            categoryRepository.save(category3);
            Category category4 = new Category("한식", 0L);

            categoryRepository.save(category4);
            Category category5 = new Category("중식", 0L);

            categoryRepository.save(category5);
            Category category6 = new Category("양식", 0L);

            categoryRepository.save(category6);
            Category category7 = new Category("일식/회", 0L);

            categoryRepository.save(category7);
            Category category8 = new Category("디저트", 0L);

            categoryRepository.save(category8);
            Category category9 = new Category("채식", 0L);

            categoryRepository.save(category9);
            Category category10 = new Category("아시안", 0L);

            categoryRepository.save(category10);
            Category category11 = new Category("채식", 0L);

            categoryRepository.save(category11);
            Category category12 = new Category("건강식", 0L);

            categoryRepository.save(category12);
            Category category13 = new Category("샌드위치", 0L);

            categoryRepository.save(category13);
            Category category14 = new Category("편의점", 0L);

            categoryRepository.save(category14);
            Category category15 = new Category("전체", 0L);

            categoryRepository.save(category15);
        }

        // 지역
        if (regionRepository.count() == 0) {
            Region region0 = new Region("서울", 0L);
            regionRepository.save(region0);
            Region region1 = new Region("경기", 0L);
            regionRepository.save(region1);
            Region region2 = new Region("인천", 0L);
            regionRepository.save(region2);
            Region region3 = new Region("강원", 0L);
            regionRepository.save(region3);
            Region region4 = new Region("충북", 0L);
            regionRepository.save(region4);
            Region region5 = new Region("충남", 0L);
            regionRepository.save(region5);
            Region region6 = new Region("전북", 0L);
            regionRepository.save(region6);
            Region region7 = new Region("전남", 0L);
            regionRepository.save(region7);
            Region region8 = new Region("경북", 0L);
            regionRepository.save(region8);
            Region region9 = new Region("경남", 0L);
            regionRepository.save(region9);
            Region region10 = new Region("제주", 0L);
            regionRepository.save(region10);
        }

        //게시글 등록
        Post post = Post.builder()
                .roomTitle(requestDto.getRoomTitle()) // 채팅방 제목
                .member(member)
                .isDone(requestDto.isDone())// 모집중
                .content(requestDto.getContent()) // 내용
                .category(requestDto.getCategory()) //카테고리
                .maxCapacity(requestDto.getMaxCapacity()) // 최대 인원
                .region(requestDto.getRegion()) // 지역
                .targetAddress(requestDto.getTargetAddress()) // 식당주소
                .targetName(requestDto.getTargetName())// 식당이름
                .targetAmount(requestDto.getTargetAmount())// 목표금액
                .deliveryTime(requestDto.getDeliveryTime()) // 배달시간
                .deliveryFee(requestDto.getDeliveryFee()) // 배달요금
                .participantNumber(requestDto.getParticipantNumber()) // 참여자수
                .gatherName(requestDto.getGatherName()) // 모이는 장소 이름
                .gatherAddress(requestDto.getGatherAddress()) // 모이는 장소 주소
                .hits(requestDto.getHits()) // 조회수
                .limitTime(requestDto.getLimitTime()) // 파티모집 마감 시각
                .nickname(requestDto.getNickname()) // 닉네임
                .profileURL(requestDto.getProfileURL()) // 프로필 사진
                .build();
        postRepository.save(post);
        chatRoomService.createChatRoom(post, request); //채팅방 자동생성
        chatRoomService.enterRoom(post.getId(), request); //게시글작성자 자동입장
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId()) //게시글 번호
                        .memberId(post.getMember().getId()) // 회원 번호
                        .content(post.getContent()) // 게시글 내용
                        .roomTitle(post.getRoomTitle()) // 채팅방 제목
                        .isDone(post.isDone())// 모집중
                        .isClosed(post.isClosed()) // 나간방
                        .category(post.getCategory()) //카테고리
                        .maxCapacity(post.getMaxCapacity()) // 최대인원
                        .region(post.getRegion()) // 지역
                        .targetAddress(post.getTargetAddress()) // 식당주소
                        .targetName(post.getTargetName())// 식당이름
                        .targetAmount(post.getTargetAmount())// 목표금액
                        .deliveryTime(post.getDeliveryTime()) // 배달시간
                        .deliveryFee(post.getDeliveryFee()) // 배달요금
                        .participantNumber(post.getParticipantNumber()) // 참여자수
                        .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                        .gatherName(post.getGatherName()) // 모이는 장소 이름
                        .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                        .hits(post.getHits()) // 조회수
                        .createdAt(post.getCreatedAt()) // 생성일
                        .modifiedAt(post.getModifiedAt()) // 수정일
                        .nickname(post.getMember().getNickname()) // 작성자 닉네임
                        .profileURL(post.getMember().getProfileURL()) // 작성자 프로필 사진
                        .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                        .build()
        );
    }

    // 카테고리 게시글 조회
    @Transactional
    public ResponseDto<?> findCategoryPost(String category) {
        List<Post> postList = postRepository.findAllByCategory(category);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : postList) {
            if (post.isDone() == false ) {
                postResponseDtoList.add(
                        PostResponseDto.builder()
                                .postId(post.getId()) //게시글 아이디
                                .memberId(post.getMember().getId()) // 게시글 ID
                                .content(post.getContent()) // 게시글 내용
                                .roomTitle(post.getRoomTitle()) // 채팅방 제목
                                .isDone(post.isDone())// 모집중
                                .isClosed(post.isClosed()) // 나간방
                                .region(post.getRegion()) // 지역
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
                                .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                                .createdAt(post.getCreatedAt()) // 생성일
                                .modifiedAt(post.getModifiedAt()) // 수정일
                                .nickname(post.getMember().getNickname()) // 작성자 닉네임
                                .profileURL(post.getMember().getProfileURL()) // 작성자 프로필 사진
                                .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                                .build()
                );
            }
        }
        return ResponseDto.success(postResponseDtoList);
    }

    // 전체 게시글 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {
        List<Post> allPosts = postRepository.findAllByOrderByModifiedAtDesc();
        List<GetAllPostResponseDto> getAllPostResponseDtoList = new ArrayList<>();

        for (Post post : allPosts) {
            if (post.isDone() == false ) {
                getAllPostResponseDtoList.add(
                        GetAllPostResponseDto.builder()
                                .postId(post.getId()) // 게시글 번호
                                .memberId(post.getMember().getId()) // 회원 번호
                                .content(post.getContent()) // 게시글 내용
                                .roomTitle(post.getRoomTitle()) // 채팅방 제목
                                .region(post.getRegion()) // 지역
                                .isDone(post.isDone())// 모집중
                                .isClosed(post.isClosed()) // 나간방
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
                                .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                                .createdAt(post.getCreatedAt()) // 생성일
                                .modifiedAt(post.getModifiedAt()) // 수정일
                                .nickname(post.getMember().getNickname()) // 작성자 닉네임
                                .profileURL(post.getMember().getProfileURL()) // 작성자 프로필 사진
                                .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                                .build()
                );
            }
        }
        return ResponseDto.success(getAllPostResponseDtoList);
    }

    //게시글 수정
    @Transactional
    public ResponseDto<PostResponseDto> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

        // Refresh_Token 유효성 검사
        if (null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // Authorization 유효성 검사
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // 토큰 유효성 검사
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 게시글 유효성 검사
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        // 회원 유효성 검사
        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }
        post.update(requestDto);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId()) // 게시글 번호
                        .memberId(post.getMember().getId()) // 회원 번호
                        .content(post.getContent()) // 게시글 내용
                        .roomTitle(post.getRoomTitle()) // 채팅방 제목
                        .region(post.getRegion()) // 지역
                        .isDone(post.isDone())// 모집중
                        .isClosed(post.isClosed()) // 나간방
                        .maxCapacity(post.getMaxCapacity()) // 최대인원
                        .category(post.getCategory()) //카테고리
                        .targetAddress(post.getTargetAddress()) // 식당주소
                        .targetName(post.getTargetName())// 식당이름
                        .targetAmount(post.getTargetAmount())// 목표금액
                        .deliveryTime(post.getDeliveryTime()) // 배달시간
                        .deliveryFee(post.getDeliveryFee()) // 배달요금
                        .participantNumber(post.getParticipantNumber()) // 참여자수
                        .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                        .gatherName(post.getGatherName()) // 모이는 장소 이름
                        .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                        .hits(post.getHits()) // 조회수
                        .createdAt(post.getCreatedAt()) // 생성일
                        .modifiedAt(post.getModifiedAt()) // 수정일
                        .nickname(post.getMember().getNickname()) // 작성자 닉네임
                        .profileURL(post.getMember().getProfileURL()) // 작성자 프로필 사진
                        .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                        .build()
        );
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
        // Refresh_Token 유효성검사
        if (null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // Authorization 유효성 검사
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // Token 유효성 검사
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 게시글 유효성 검사
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        // 회원 유효성 검사
        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        ChatRoom chatRoom = chatRoomRepository.findById(id).get();
        chatRoomMemberJpaRepository.deleteAllByChatRoom(chatRoom); //채팅방안 멤버 모두 삭제
        chatRoomRepository.deleteById(id);  // 채팅방도 같이 삭제
        postRepository.delete(post);   // 게시글 삭제
        return ResponseDto.success("delete success");
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    // Refresh_Token 유효성 검사
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    // 특정 게시물 조회
    public ResponseDto<?> getPost(Long id) {
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId()) //게시글 아이디
                        .memberId(post.getMember().getId()) // 게시글 ID
                        .content(post.getContent()) // 게시글 내용
                        .roomTitle(post.getRoomTitle()) // 채팅방 제목
                        .region(post.getRegion()) // 지역
                        .isDone(post.isDone())// 모집중
                        .isClosed(post.isClosed()) // 나간방
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

    // 게시글 상세 페이지 조회
    @Transactional
    public ResponseDto<?> getDetailPost(Long postId, HttpServletRequest request) {

        // 게시글 유효성 체크
        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND_POST", "해당 게시글이 없습니다.");
        }
        //회원 유효성 체크
        Member member = validateMember(request);
        LocalDateTime now = LocalDateTime.now();

        if (now.isEqual(post.getLimitTime()) || now.isAfter(post.getLimitTime())) {
            post.isDone(false);
            postRepository.save(post);
        }

        Long memId = post.getMember().getId();

        Hits hits = new Hits(memId, postId);

        hitsRepository.save(hits);
        post.hitsPost();
        postRepository.save(post);

        String cate = post.getCategory();
        Long cateHits = post.getHits();
        Category category = isPresentCategory(cate);

        if (categoryRepository.existsByCategory(cate)) {
            category.updateCateHits(cate);
            categoryRepository.save(category);
        }

        if (category == null) {
            category = new Category(cate, cateHits);
            categoryRepository.save(category);
        }

        return ResponseDto.success(
                PostDetailResponseDto.builder()
                        .postId(post.getId()) //게시글 아이디
                        .memberId(post.getMember().getId()) // 게시글 ID
                        .content(post.getContent()) // 게시글 내용
                        .roomTitle(post.getRoomTitle()) // 채팅방 제목
                        .region(post.getRegion()) // 지역
                        .isDone(post.isDone())// 모집중
                        .isClosed(post.isClosed()) // 나간방
                        .category(post.getCategory()) //카테고리
                        .maxCapacity(post.getMaxCapacity()) // 최대인원
                        .targetAddress(post.getTargetAddress()) // 식당주소
                        .targetName(post.getTargetName())// 식당이름
                        .targetAmount(post.getTargetAmount())// 목표금액
                        .deliveryTime(post.getDeliveryTime()) // 배달시간
                        .deliveryFee(post.getDeliveryFee()) // 배달요금
                        .participantNumber(Math.toIntExact(post.getParticipantNumber())) // 참여자수
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

    // 카테고리 유효성 검사
    @Transactional(readOnly = true)
    public Category isPresentCategory(String category) {
        Optional<Category> optionalCategory = categoryRepository.findByCategory(category);
        return optionalCategory.orElse(null);
    }

    // 참가자수 증가
    @Transactional
    public ResponseDto<?> updateParticipantNumber(Long postId, HttpServletRequest request) {
        // 게시글 유효성 체크
        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND_POST", "해당 게시글이 없습니다.");
        }
        // 참가자수 증가
        post.updateParticipantNumber();
        // 참가자수 post 테이블에 저장
        postRepository.save(post);
        return ResponseDto.success(post.getParticipantNumber());
    }

    // 참가자수 감소
    @Transactional
    public ResponseDto<?> decreaseParticipant(Long postId, HttpServletRequest request) {
        // 게시글 유효성 체크
        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND_POST", "해당 게시글이 없습니다.");
        }
        // 참가자수 감소
        post.decreaseParticipantNumber();
        // 참가자수 post 테이블에 저장
        postRepository.save(post);
        return ResponseDto.success(post.getParticipantNumber());
    }
}