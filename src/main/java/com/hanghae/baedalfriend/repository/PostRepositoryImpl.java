package com.hanghae.baedalfriend.repository;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import com.hanghae.baedalfriend.dto.requestdto.QPostRequestDto;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import static com.hanghae.baedalfriend.domain.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostRequestDto> fullTextSearch(String keyword) {
        StringTemplate StringTemplate = Expressions.stringTemplate("MATCH({roomTitle})  AGAINST('keyword*'IN BOOLEAN MODE)", post.roomTitle, keyword);

        return queryFactory
                .select(new QPostRequestDto(
                        post.id,
                        post.roomTitle,
                        StringTemplate.as("isMatch")
                ))
                .from(post)
                .where(post.roomTitle.contains(keyword).or(post.content.contains(keyword)))
                .fetch();
    }

    @Override
    public List<Post> searchAll() {
        EntityPath<Post> post = null;
        return queryFactory
                .selectFrom(post)
                .fetch();
    }

    @Override
    public Page<Post> findByRoomTitleContaining(String keyword, Pageable pageable) {
         return (Page<Post>) queryFactory
                 .selectFrom(post)
                 .where(post.roomTitle.contains(keyword))
                 .fetchResults()
                 .getResults();
     }
}