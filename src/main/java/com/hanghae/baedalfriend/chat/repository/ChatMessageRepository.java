package com.hanghae.baedalfriend.chat.repository;


import com.hanghae.baedalfriend.chat.dto.response.ChatMessageResponseDto;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Repository
public class ChatMessageRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, List<ChatMessage>> opsHashOperationMessage;
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatMessage> hashOpsChatMessage;

    @PostConstruct
    private void init() {
        opsHashOperationMessage = redisTemplate.opsForHash();
    }

    public ChatMessageResponseDto save(ChatMessage chatMessage) {
        System.out.println("chatMessage is + " + chatMessage);
        hashOpsChatMessage.put(chatMessage.getTitle(), chatMessage.getSender(),chatMessage);

        return  ChatMessageResponseDto.builder()
                .title(chatMessage.getTitle())
                .message(chatMessage.getMessage())
                .sender(chatMessage.getSender())
                .build();
    }

    public void delete(Long roomId) {
        opsHashOperationMessage.delete(CHAT_MESSAGE, roomId);
    }

    public List<ChatMessage> findAllMessage(Long roomId) {
        // Deserialize = 예시) MultiPartFile --> File
        log.info("Data Passed");

        return opsHashOperationMessage.get(CHAT_MESSAGE, roomId);
    }
}

