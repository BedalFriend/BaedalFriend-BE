package com.hanghae.baedalfriend.controller;

import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1/posts")
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // 제목 + 키워드 검색 + 정렬 기능
    @RequestMapping(value= "/search", method = RequestMethod.GET)
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

    // 카테고리 검색 + 정렬 기능
    @RequestMapping(value= "/category/search", method = RequestMethod.GET)
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

   // 지역별 검색 + 정렬 기능
    @RequestMapping(value= "/region/search", method = RequestMethod.GET)
    public ResponseDto<?> getRegionSearch(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                    @RequestParam("keyword") String keyword,
                                    @RequestParam("sortBy") String sortBy,
                                    @RequestParam("isAsc") boolean isAsc) {
        {
            page = page - 1;
            return searchService.getRegionSearch(keyword, page, size, sortBy, isAsc);
        }
    }

    // 인기 검색
//    @RequestMapping(value = "/popularSearchWord", method = RequestMethod.GET)
//    public ResponseDto<?> getSearchPopular(){
//        return searchService.getSearchPopular();
//    }

    // 최근 검색
//    @RequestMapping(value = "/recentSearchTerms", method = RequestMethod.GET)
//    public ResponseDto<?> getSearchRecentTerms(HttpServletRequest request){
//        return searchService.getSearchRecentTerms(request);
//    }

}