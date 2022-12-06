package com.hanghae.baedalfriend.service;

import com.hanghae.baedalfriend.chat.repository.ChatRoomJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberJpaRepository;
import com.hanghae.baedalfriend.chat.service.ChatRoomService;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.dto.responsedto.PostResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private ChatRoomService chatRoomService;
    private ChatRoomJpaRepository chatRoomRepository;
    private ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
    private ChatRoomJpaRepository chatRoomJpaRepository;
    private PostRepository postRepository;
    private TokenProvider tokenProvider;
    private PostService postService;

    public SearchService(ChatRoomService chatRoomService, ChatRoomJpaRepository chatRoomRepository, ChatRoomMemberJpaRepository chatRoomMemberJpaRepository, ChatRoomJpaRepository chatRoomJpaRepository, PostRepository postRepository, TokenProvider tokenProvider, PostService postService) {
        this.chatRoomService = chatRoomService;
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberJpaRepository = chatRoomMemberJpaRepository;
        this.chatRoomJpaRepository = chatRoomJpaRepository;
        this.postRepository = postRepository;
        this.tokenProvider = tokenProvider;
        this.postService = postService;
    }

    //특정 카테고리별 검색 + 정렬
    @Transactional
    public ResponseDto<?> getCategorySearch(String keyword, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findByCategory(keyword, sortBy, pageable);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Post post : posts) {
            if (now.isEqual(post.getLimitTime()) || now.isAfter(post.getLimitTime())) {
                post.isDone(false);
                postRepository.save(post);
            }
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .postId(post.getId())
                            .memberId(post.getMember().getId())
                            .roomTitle(post.getRoomTitle()) // 채팅방 제목
                            .isDone(post.isDone()) // 모집중
                            .region(post.getRegion()) // 지역
                            .category(post.getCategory()) //카테고리
                            .targetAddress(post.getTargetAddress()) // 식당주소
                            .targetName(post.getTargetName()) // 식당이름
                            .targetAmount(post.getTargetAmount()) // 목표금액
                            .deliveryTime(post.getDeliveryTime()) // 배달시간
                            .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
                            .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                            .createdAt(post.getCreatedAt()) // 생성일
                            .modifiedAt(post.getModifiedAt()) // 수정일
                            .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }

    // 제목 & 카테고리 키워드 검색 + 정렬 기준
    @Transactional
    public ResponseDto<?> getSearch(String keyword, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findByRoomTitle(keyword, sortBy, pageable);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Post post : posts) {
            if (now.isEqual(post.getLimitTime()) || now.isAfter(post.getLimitTime())) {
                post.isDone(false);
                postRepository.save(post);
            }
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .postId(post.getId())
                            .memberId(post.getMember().getId())
                            .roomTitle(post.getRoomTitle()) // 채팅방 제목
                            .isDone(post.isDone()) // 모집중
                            .region(post.getRegion()) // 지역
                            .category(post.getCategory()) //카테고리
                            .targetAddress(post.getTargetAddress()) // 식당주소
                            .targetName(post.getTargetName()) // 식당이름
                            .targetAmount(post.getTargetAmount()) // 목표금액
                            .deliveryTime(post.getDeliveryTime()) // 배달시간
                            .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
                            .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                            .createdAt(post.getCreatedAt()) // 생성일
                            .modifiedAt(post.getModifiedAt()) // 수정일
                            .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList); // 검색결과 반환
    }

    //제목 , 지역 검색 + 정렬
    @Transactional
    public ResponseDto<?> getRegionSearch(String keyword, String region, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findByRoomTitleAndRegion(keyword, region, sortBy, pageable);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Post post : posts) {
            if (now.isEqual(post.getLimitTime()) || now.isAfter(post.getLimitTime())) {
                post.isDone(false);
                postRepository.save(post);
            }
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .postId(post.getId())
                            .memberId(post.getMember().getId())
                            .roomTitle(post.getRoomTitle()) // 채팅방 제목
                            .isDone(post.isDone()) // 모집중
                            .region(post.getRegion()) // 지역
                            .category(post.getCategory()) //카테고리
                            .targetAddress(post.getTargetAddress()) // 식당주소
                            .targetName(post.getTargetName()) // 식당이름
                            .targetAmount(post.getTargetAmount()) // 목표금액
                            .deliveryTime(post.getDeliveryTime()) // 배달시간
                            .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
                            .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                            .createdAt(post.getCreatedAt()) // 생성일
                            .modifiedAt(post.getModifiedAt()) // 수정일
                            .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList); // 검색결과 반환
    }

    // 지역 검색  (전체 카테고리) + 정렬 ( 로그인 후 )
    @Transactional
    public ResponseDto<?> getRegionEntireCategory(String keyword, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findByRegion(keyword, sortBy, pageable);

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        for (Post post : posts) {
            if (now.isEqual(post.getLimitTime()) || now.isAfter(post.getLimitTime())) {
                post.isDone(false);
                postRepository.save(post);
            }
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .postId(post.getId())
                            .memberId(post.getMember().getId())
                            .roomTitle(post.getRoomTitle()) // 채팅방 제목
                            .isDone(post.isDone()) // 모집중
                            .region(post.getRegion()) // 지역
                            .category(post.getCategory()) //카테고리
                            .targetAddress(post.getTargetAddress()) // 식당주소
                            .targetName(post.getTargetName()) // 식당이름
                            .targetAmount(post.getTargetAmount()) // 목표금액
                            .deliveryTime(post.getDeliveryTime()) // 배달시간
                            .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
                            .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                            .createdAt(post.getCreatedAt()) // 생성일
                            .modifiedAt(post.getModifiedAt()) // 수정일
                            .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }

    //    (전체 카테고리) 검색 + 정렬
    @Transactional
    public ResponseDto<?> getEntireCategory(String keyword, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findByRoomTitle(keyword, sortBy, pageable);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Post post : posts) {
            if (now.isEqual(post.getLimitTime()) || now.isAfter(post.getLimitTime())) {
                post.isDone(false);
                postRepository.save(post);
            }
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .postId(post.getId())
                            .memberId(post.getMember().getId())
                            .roomTitle(post.getRoomTitle()) // 채팅방 제목
                            .isDone(post.isDone()) // 모집중
                            .region(post.getRegion()) // 지역
                            .category(post.getCategory()) //카테고리
                            .targetAddress(post.getTargetAddress()) // 식당주소
                            .targetName(post.getTargetName()) // 식당이름
                            .targetAmount(post.getTargetAmount()) // 목표금액
                            .deliveryTime(post.getDeliveryTime()) // 배달시간
                            .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
                            .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                            .createdAt(post.getCreatedAt()) // 생성일
                            .modifiedAt(post.getModifiedAt()) // 수정일
                            .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }

    //지역 , 카테고리별 검색 + 정렬 기능 ( 로그인 후 현재 위치를 입력한 사용자 )
    @Transactional
    public ResponseDto<?> getRegionCategorySearch(String keyword, String region, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findByCategoryAndRegion(keyword, region, sortBy, pageable);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Post post : posts) {
            if (now.isEqual(post.getLimitTime()) || now.isAfter(post.getLimitTime())) {
                post.isDone(false);
                postRepository.save(post);
            }
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .postId(post.getId())
                            .memberId(post.getMember().getId())
                            .roomTitle(post.getRoomTitle()) // 채팅방 제목
                            .isDone(post.isDone()) // 모집중
                            .region(post.getRegion()) // 지역
                            .category(post.getCategory()) //카테고리
                            .targetAddress(post.getTargetAddress()) // 식당주소
                            .targetName(post.getTargetName()) // 식당이름
                            .targetAmount(post.getTargetAmount()) // 목표금액
                            .deliveryTime(post.getDeliveryTime()) // 배달시간
                            .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
                            .chatRoomMembers(chatRoomMemberJpaRepository.findAllByChatRoom(chatRoomJpaRepository.findById(post.getId()).get())) //참여중인 유저목록
                            .createdAt(post.getCreatedAt()) // 생성일
                            .modifiedAt(post.getModifiedAt()) // 수정일
                            .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }
}