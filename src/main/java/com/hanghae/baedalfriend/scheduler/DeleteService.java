package com.hanghae.baedalfriend.scheduler;

import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteService {


    //isdone값변경
    @Transactional
    public void updateIsDone(Post post) {
        post.isDone(true);
    }
//
//    public void delete(Post post){
//
//    }
}
