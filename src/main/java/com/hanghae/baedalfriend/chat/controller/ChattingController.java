package com.hanghae.baedalfriend.chat.controller;

import com.hanghae.baedalfriend.chat.dto.request.ChatMessageRequestDto;
import com.hanghae.baedalfriend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ChattingController {
    private final ChatService chatService;

    //pub/chat/message/ 에서 들어오는 메시지 처리
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageRequestDto, HttpServletRequest request) {
        chatService.save(messageRequestDto,request);
    }
}