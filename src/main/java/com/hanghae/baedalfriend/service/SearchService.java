package com.hanghae.baedalfriend.service;

import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.PopularSearchWord;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.domain.RecentSearchTerms;
import com.hanghae.baedalfriend.dto.responsedto.PopularSearchWordResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.PostResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.RecentSearchTermsResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
//import com.hanghae.baedalfriend.repository.PopularSearchWordRepository;
import com.hanghae.baedalfriend.repository.PostRepository;
//import com.hanghae.baedalfriend.repository.RecentSearchTermsRepository;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private PostRepository postRepository;
    private TokenProvider tokenProvider;
    private PostService postService;
//    private PopularSearchWordRepository popularSearchWordRepository;
//    private RecentSearchTermsRepository recentSearchTermsRepository;

    public SearchService(PostRepository postRepository, TokenProvider tokenProvider, PostService postService) {
        this.postRepository = postRepository;
        this.tokenProvider = tokenProvider;
        this.postService = postService;
    }

    // 카테고리별 검색 + 정렬
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
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
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
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
                            .createdAt(post.getCreatedAt()) // 생성일
                            .modifiedAt(post.getModifiedAt()) // 수정일
                            .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                            .build()
            );
        }
//        if(!popularSearchWordRepository.existsBySearchWord(keyword)) {
//            PopularSearchWord popularSearchWord = new PopularSearchWord(keyword);
//            popularSearchWordRepository.save(popularSearchWord);
//        }else {
//            PopularSearchWord popularSearchWord = popularSearchWordRepository.findBySearchWord(keyword);
//            popularSearchWord.updateCount();
//            popularSearchWordRepository.save(popularSearchWord);
//        }
        return ResponseDto.success(postResponseDtoList);
    }


    // 인기 검색
//    @Transactional
//    public ResponseDto<?> getSearchPopular() {
//        List<PopularSearchWord> popularSearchWords = popularSearchWordRepository.findAllByOrderBySearchWordHitsDesc();
//        List<PopularSearchWordResponseDto> popularSearchWordResponseDto = new ArrayList<>();
//
//        for (PopularSearchWord popularSearchWord : popularSearchWords) {
//            popularSearchWordResponseDto.add(
//                    PopularSearchWordResponseDto.builder()
//                            .searchWord(popularSearchWord.getSearchWord())
//                            .searchWordHits(popularSearchWord.getSearchWordHits())
//                            .build()
//            );
//
//            if (popularSearchWordResponseDto.size() >= 5) break;
//        }
//        return ResponseDto.success(popularSearchWordResponseDto);
//    }

    // 최근 검색
//    public ResponseDto<?> getSearchRecentTerms(HttpServletRequest request) {
//        // 회원인지 체크
//        Member member = validateMember(request);
//        if (member == null) {
//            return ResponseDto.success("회원에게만 제공되는 서비스입니다.");
//        }
//
//        List<RecentSearchTerms> recentSearchTerms = recentSearchTermsRepository.findAllByIdOrderByModifiedAtDesc(member.getId());
//        List<RecentSearchTermsResponseDto> recentSearchTermsResponseDto = new ArrayList<>();
//
//        for (RecentSearchTerms recentSearchTerm : recentSearchTerms) {
//            recentSearchTermsResponseDto.add(
//                    RecentSearchTermsResponseDto.builder()
//                            .searchWord(recentSearchTerm.getSearchWord())
//                            .searchTime(recentSearchTerm.getModifiedAt())
//                            .build()
//            );
//            if (recentSearchTermsResponseDto.size() >= 10) break;
//        }
//        return ResponseDto.success(recentSearchTermsResponseDto);
//    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    // 지역검색 + 키워드
    public ResponseDto<?> getRegionSearch(String keyword, int page, int size, String sortBy, boolean isAsc) {
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
                            .region(post.getRegion()) // 지역
                            .roomTitle(post.getRoomTitle()) // 채팅방 제목
                            .isDone(post.isDone()) // 모집중
                            .category(post.getCategory()) //카테고리
                            .targetAddress(post.getTargetAddress()) // 식당주소
                            .targetName(post.getTargetName()) // 식당이름
                            .targetAmount(post.getTargetAmount()) // 목표금액
                            .deliveryTime(post.getDeliveryTime()) // 배달시간
                            .deliveryFee(post.getDeliveryFee()) // 배달요금
                            .participantNumber(post.getParticipantNumber()) // 참여자수
                            .gatherName(post.getGatherName()) // 모이는 장소 이름
                            .gatherAddress(post.getGatherAddress()) // 모이는 장소 주소
                            .hits(post.getHits()) // 조회수
                            .createdAt(post.getCreatedAt()) // 생성일
                            .modifiedAt(post.getModifiedAt()) // 수정일
                            .limitTime(post.getLimitTime()) // 파티모집 마감 시각
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }
}