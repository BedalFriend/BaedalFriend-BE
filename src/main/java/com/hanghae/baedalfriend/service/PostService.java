package com.hanghae.baedalfriend.service;

import com.hanghae.baedalfriend.domain.Category;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.CategoryOnlyResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.GetAllPostResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.PostResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.CategoryRepository;
import com.hanghae.baedalfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TokenProvider tokenProvider;

    // 게시글 등록
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto,
                                     HttpServletRequest request) throws Exception {

        if (null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        //게시글 등록
        Post post = Post.builder()
                .roomTitle(requestDto.getRoomTitle()) // 채팅방 제목
                .content(requestDto.getContent()) // 게시글 내용
                .member(member)
                .limitTime(Math.toIntExact(requestDto.getLimitTime())) // 게시글의 파티원 모집기간
                .gather(requestDto.getGather()) // 집결지 주소
                .category(requestDto.getCategory()) //카테고리
                .imageUrl(requestDto.getImageUrl()) // imageUrl
                .target(requestDto.getTarget()) // 식당 주소
                .maxCapacity(requestDto.getMaxCapacity()) // 최대수용인원
                .build();
        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId()) // 게시글 ID
                        .roomTitle(post.getRoomTitle()) // 채팅방 제목
                        .gather(post.getGather()) // 집결지 주소
                        .memberId(post.getMember().getId()) // 멤버 ID
                        .nickname(post.getMember().getNickname()) // 게시글 작성자 닉네임
                        .category(post.getCategory()) // 카테고리
                        .imageUrl(post.getImageUrl()) // s3 2022-11-11 merge 예정 대비
                        .limitTime(post.getLimitTime()) //게시글의 파티원 모집기간
                        .target(post.getTarget()) // 식당 주소
                        .content(post.getContent()) // 게시글 내용
                        .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                        .build()
        );
    }

    // 전체 게시글 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {

        List<Post> allPosts = postRepository.findAllByOrderByModifiedAtDesc();
        List<GetAllPostResponseDto> getAllPostResponseDtoList = new ArrayList<>();

        for(Post post : allPosts) {
            getAllPostResponseDtoList.add(
                    GetAllPostResponseDto.builder()
                            .id(post.getId())
                            .roomTitle(post.getRoomTitle()) // 채팅방 제목
                            .maxCapacity(post.getMaxCapacity()) //최대수용 인원
                            .nickname(post.getMember().getNickname()) // 게시글 작성자 닉네임
                            .target(post.getTarget()) // 식당
                            .gather(post.getGather()) // 집결지주소
                            .imageUrl(post.getImageUrl()) // 이미지url
                            .category(post.getCategory()) // 카테고리
                            .content(post.getContent()) // 게시글 내용
                            .createdAt(post.getCreatedAt()) // 게시글 생성시간
                            .modifiedAt(post.getModifiedAt()) // 게시글 수정시간
                            .limitTime(post.getLimitTime()) // 파티참여 모집시간
                            .build()
            );
        }
        return ResponseDto.success(getAllPostResponseDtoList);
    }

    //게시글 수정
    @Transactional
    public ResponseDto<PostResponseDto> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        post.update(requestDto);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId())
                        .roomTitle(post.getRoomTitle()) // 채팅방 제목
                        .content(post.getContent()) // 게시글 내용
                        .gather(post.getGather()) // 집결지주소
                        .limitTime(post.getLimitTime()) // 게시글의 파티원 모집 마감시각
                        .category(post.getCategory()) // 카테고리
                        .target(post.getTarget()) // 식당
                        .nickname(post.getMember().getNickname()) // 게시글 작성자 닉네임
                        .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                        .imageUrl(post.getImageUrl()) // 이미지Url
                        .build()
        );
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
        if (null == request.getHeader("Refresh_Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
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

    public ResponseDto<?> getPost(Long id) {
        Post post = isPresentPost(id);
        if(null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId())
                        .roomTitle(post.getRoomTitle()) // 채팅방 제목
                        .nickname(post.getMember().getNickname()) // 게시글 작성자 닉네임
                        .target(post.getTarget()) // 식당 주소
                        .gather(post.getGather()) // 집결지 주소
                        .limitTime(post.getLimitTime()) //게시글의 파티원  모집 마감시각
                        .category(post.getCategory()) // 카테고리
                        .content(post.getContent()) // 게시글 내용
                        .maxCapacity(post.getMaxCapacity()) // 최대수용인원
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> showCategoryPost() {
        List<Category> categories = categoryRepository.findAllByOrderByCategoryAsc();
        List<CategoryOnlyResponseDto> categoryOnlyResponseDtos = new ArrayList<>();

        for (Category category : categories) {
            categoryOnlyResponseDtos.add(
                    CategoryOnlyResponseDto.builder()
                            .categoryName(category.getCategory())
                            .build()
            );
        }

        return ResponseDto.success(categoryOnlyResponseDtos);
    }

    @Transactional
    public ResponseDto<?> findCategoryPost(String category) {
        List<Post> postList = postRepository.findAllByCategory(category);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for(Post post: postList) {
            postResponseDtoList.add (
                    PostResponseDto.builder()
                            .postId(post.getId())
                            .roomTitle(post.getRoomTitle())
                            .nickname(post.getNickname())
                            .imageUrl(post.getImageUrl())
                            .content(post.getContent())
                            .limitTime(post.getLimitTime())
                            .category(post.getCategory())
                           // .createdAt(post.getCreatedAt())
                         //   .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }

}