package com.hanghae.baedalfriend.scheduler;


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

    @Scheduled(cron = "0 0/3 * * * ?") //1시간마다
    public void userJobHour() throws Exception {

    }


    public static void main(String[] args) {


    }


    public void softDelete() {
        List<Post> postList = postRepository.findAll();
        for (Post post : postList) {
            //인자보다 관거일 떄 true return
            if (now.isBefore(post.getLimitTime())) {
                deleteService.updateIsDone(post);
            }
        }


    }


////    public void hardDelete(){
////        List<Post> postList = postRepository.findAll();
////        for (Post post : postList) {
////            if (post.isDone()) {
////
////
////
////
////            }
////   }


}


