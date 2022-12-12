package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostRequestDto> fullTextSearch(String keyword);

    List<Post> searchAll();

    Page<Post> findByRoomTitleContaining(String keyword, Pageable pageable);
}
