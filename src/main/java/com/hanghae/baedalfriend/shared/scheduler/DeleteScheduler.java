package com.hanghae.baedalfriend.shared.scheduler;


import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteScheduler {


    private final PostRepository postRepository;
    private final DeleteService deleteService;
    LocalDateTime now = LocalDateTime.now();


    @Scheduled(cron = "0 0/1 * * * ?  ") //1분마다 실행
    public void userJobMin() throws Exception {
        softDelete();
    }

    @Scheduled(cron = "0 0 10 L * ?") //매월 마지막날 저녁 10시에 실행
    public void userJob() throws Exception {
        hardDelete();

    }


    public static void main(String[] args) {


    }


    public void softDelete() {
        List<Post> postList = postRepository.findAll();
        for (Post post : postList) {
            //인자보다 과거일 떄 true return
            if (now.isBefore(post.getLimitTime())) {
                deleteService.updateIsDone(post);
            }
        }


    }


    public void hardDelete() {
        List<Post> postList = postRepository.findAll();
        for (Post post : postList) {
            if (post.isClosed()) {
                deleteService.delete(post);
            }
        }
    }


}


