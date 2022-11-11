package com.hanghae.baedalfriend.controller;

import com.hanghae.baedalfriend.chat.repository.ChatRoomRepository;
import com.hanghae.baedalfriend.chat.service.ChatRoomService;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.service.PostService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class PostController {
    private final PostService postService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Refresh_Token",
                    required = true,
                    dataType = "string",
                    paramType = "header"
            )
    })

    // 게시물 등록
    @PostMapping(value = "/auth/posts")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
                                     HttpServletRequest request) throws Exception {
        chatRoomService.createChatRoom(requestDto, request);
        return postService.createPost(requestDto, request);
    }

    // 전체 게시물 조회
    @GetMapping(value = "/posts")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    // 특정 게시물 조회
    @GetMapping(value ="/posts/{id}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);

    }
    // 게시물 수정
    @PutMapping(value = "/auth/posts/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request) {
        return postService.updatePost(id, postRequestDto, request);
    }

    // 게시물 삭제
    @DeleteMapping(value = "/auth/posts/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     HttpServletRequest request) {
        chatRoomRepository.deleteById(id);

        return postService.deletePost(id, request);
    }

    @RequestMapping(value="/category/show", method = RequestMethod.GET)
    public ResponseDto<?> showCategoryPost(){
        return postService.showCategoryPost();
    }
}