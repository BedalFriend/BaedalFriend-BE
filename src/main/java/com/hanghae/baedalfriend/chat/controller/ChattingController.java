package com.hanghae.baedalfriend.chat.controller;

import com.hanghae.baedalfriend.chat.dto.request.ChatMessageRequestDto;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ChattingController {
    private final ChatService chatService;

    //pub/chat/message/ 에서 들어오는 메시지 처리
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageRequestDto) {
        // 메시지 생성 시간 정보
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatedNow = now.format(formatter);
        messageRequestDto.setCreatedAt(formatedNow);

        ChatMessage chatMessage = new ChatMessage(messageRequestDto);

        chatService.save(chatMessage);
    }
}