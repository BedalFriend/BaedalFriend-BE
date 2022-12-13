package com.hanghae.baedalfriend.controller;

import com.amazonaws.Response;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/posts")
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // 제목 + 키워드 검색 + 정렬 기능 (로그인 전 현재 위치 입력하지 않은 사용자)
    @GetMapping(value = "/search")
    public ResponseDto<?> getSearch(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                    @RequestParam("keyword") String keyword,
                                    @RequestParam("sortBy") String sortBy,
                                    @RequestParam("isAsc") boolean isAsc) {
        {
            page = page - 1;
            return searchService.getSearch(keyword, page, size, sortBy, isAsc);
        }
    }


    // 카테고리 검색 + 정렬 기능 (로그인 전 현재 위치 입력하지 않은 사용자)
    @GetMapping(value = "/category/search")
    public ResponseDto<?> getCategorySearch(@RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            @RequestParam("keyword") String keyword,
                                            @RequestParam("sortBy") String sortBy,
                                            @RequestParam("isAsc") boolean isAsc) {
        {
            page = page - 1;
            return searchService.getCategorySearch(keyword, page, size, sortBy, isAsc);
        }
    }

    // 지역 + 전체카테고리 검색 + 정렬 기능 (로그인 후 현재 위치 입력한 사용자)
    @GetMapping(value = "/regionEntireCategory/search")
    public ResponseDto<?> getRegionEntireCategory(@RequestParam("page") int page,
                                                  @RequestParam("size") int size,
                                                  @RequestParam("keyword") String keyword,
                                                  @RequestParam("sortBy") String sortBy,
                                                  @RequestParam("isAsc") boolean isAsc) {
        {
            page = page - 1;
            return searchService.getRegionEntireCategory(keyword, page, size, sortBy, isAsc);
        }
    }

    // 전체 카테고리 검색 + 정렬 ( 로그인 전 현재 위치 입력하지 않은 사용자)
    @GetMapping(value = "/entireCategory/search")
    public ResponseDto<?> getEntireCategory(@RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            @RequestParam("keyword") String keyword,
                                            @RequestParam("sortBy") String sortBy,
                                            @RequestParam("isAsc") boolean isAsc) {
        {
            page = page - 1;
            return searchService.getEntireCategory(keyword, page, size, sortBy, isAsc);
        }
    }

    // 지역 , 제목 검색 + 정렬 기능 ( 로그인 후 현재위치를 입력한 사용자 )
    @GetMapping(value = "/region/search")
    public ResponseDto<?> getRegionSearch(@RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          @RequestParam("keyword") String keyword,
                                          @RequestParam("region") String region,
                                          @RequestParam("sortBy") String sortBy,
                                          @RequestParam("isAsc") boolean isAsc) {
        {
            page = page - 1;
            return searchService.getRegionSearch(keyword, region, page, size, sortBy, isAsc);
        }
    }

    // 지역 , 카테고리별 검색 + 정렬 기능 ( 로그인 후 현재 위치를 입력한 사용자 )
    @GetMapping(value = "/regionCategory/search")
    public ResponseDto<?> getRegionCategorySearch(@RequestParam("page") int page,
                                                  @RequestParam("size") int size,
                                                  @RequestParam("keyword") String keyword,
                                                  @RequestParam("region") String region,
                                                  @RequestParam("sortBy") String sortBy,
                                                  @RequestParam("isAsc") boolean isAsc) {
        {
            page = page - 1;
            return searchService.getRegionCategorySearch(keyword, region, page, size, sortBy, isAsc);
        }
    }

    // 인기검색
    @GetMapping(value="/popular/search")
    public ResponseDto<?> getPopularSearch(@RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            @RequestParam("sortBy") String sortBy,
                                            @RequestParam("isAsc") boolean isAsc)   {

        {
            page = page - 1;
            return searchService.getPopularSearch(page, size, sortBy, isAsc);
        }
    }

}