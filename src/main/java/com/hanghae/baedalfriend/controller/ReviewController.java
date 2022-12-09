package com.hanghae.baedalfriend.controller;


import com.hanghae.baedalfriend.dto.requestdto.ReviewRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.service.ReviewService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class ReviewController {
    private final ReviewService reviewService;

    // 게시물 등록
    @PostMapping(value = "/auth/review/{postId}")
    public ResponseDto<?> createReview(@PathVariable Long postId, @RequestBody List<ReviewRequestDto> reviewRequestDtoList , HttpServletRequest request){
        return reviewService.createReview(postId,reviewRequestDtoList,request);
    }

}
