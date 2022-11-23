package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
    List<Post> findAllByCategory(String category);

    // 제목 검색 + 정렬
    @Query(value="SELECT * FROM post WHERE concat(room_title, category) like concat('%',:roomTitle,'%') ORDER BY :sortBy" , nativeQuery= true)
    Page<Post> findByRoomTitle(String roomTitle, String sortBy,  Pageable pageable);

    // 카테고리 검색 + 정렬
    @Query(value="SELECT * FROM post WHERE concat(room_title, category) like concat('%',:category,'%') ORDER BY :sortBy" , nativeQuery= true)
    Page<Post> findByCategory(String category, String sortBy,  Pageable pageable);

    // 지역 검색 + 정렬
    @Query(value="SELECT * FROM post WHERE concat(room_title, category,region) like concat('%',:region,'%') ORDER BY :sortBy" , nativeQuery= true)
    Page<Post> findByRegion(String region, String sortBy, Pageable pageable);
    List<Post> findByMember(Member member);

    //내가 쓴 게시글 찾기
    List<Post> findAllByMemberIdOrderByIdDesc(Long memberId);

    //내가 쓴 게시글(채팅방) 찾기 (마이페이지)
    Post findAllByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
}