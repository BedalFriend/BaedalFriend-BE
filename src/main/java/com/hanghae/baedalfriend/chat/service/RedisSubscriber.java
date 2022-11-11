package com.hanghae.baedalfriend.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    //
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate redisTemplate;
    /**
     *  Redis --메시지 발행 (Pub)--> onMessage --> 처리
     **/
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아서 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // ChatRoomMessage 객체로 매핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            log.info("destination is (ROOM NUMBER): " + chatMessage.getRoomId());
            log.info("destination is (ROOM PATH WITH NUMBER) : /sub/chat/room/" + chatMessage.getRoomId());
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
