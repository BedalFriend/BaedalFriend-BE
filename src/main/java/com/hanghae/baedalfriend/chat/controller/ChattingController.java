package com.hanghae.baedalfriend.chat.controller;

import com.hanghae.baedalfriend.chat.dto.request.ChatMessageRequestDto;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.repository.ChatRoomRepository;
import com.hanghae.baedalfriend.chat.service.ChatService;
import com.hanghae.baedalfriend.domain.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ChattingController {
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;

    //pub/chat/message/ 에서 들어오는 메시지 처리
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageRequestDto) {
        List<ChatRoom> chatroom=chatRoomRepository.findByRoomnum(messageRequestDto.getRoomId());
        ChatRoom room=new ChatRoom();
        for (ChatRoom r : chatroom){
            room=r;
        }


        // dto로 채팅 메시지 객체 생성
        ChatMessage chatMessage = new ChatMessage(messageRequestDto,room);

        // MySQL DB,레디스에 채팅 메시지 저장
        chatService.save(chatMessage);
        // 웹소캣 통신으로 토픽 구독자들에게 메시지 전송
        chatService.sendChatMessage(chatMessage);

    }
}