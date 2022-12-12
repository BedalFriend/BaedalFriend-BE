package com.hanghae.baedalfriend.repository;
import com.hanghae.baedalfriend.domain.Post;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> searchAll() {
        EntityPath<Post> post = null;
        return queryFactory
                .selectFrom(post)
                .fetch();
    }


}