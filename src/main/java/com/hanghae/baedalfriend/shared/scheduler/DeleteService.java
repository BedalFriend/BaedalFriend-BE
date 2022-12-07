package com.hanghae.baedalfriend.shared.scheduler;

import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteService {
    private final PostRepository postRepository;


    //isdone값변경
    @Transactional
    public void updateIsDone(Post post) {
        post.isDone(true);
        postRepository.save(post);
    }

    public void delete(Post post){
        postRepository.delete(post);
    }
}
