package com.hanghae.baedalfriend.chat.repository;



import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Repository
public class ChatMessageRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, List<ChatMessage>> opsHashOperationMessage;
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";

    @PostConstruct
    private void init() {
        opsHashOperationMessage = redisTemplate.opsForHash();
    }

    public ChatMessage save(ChatMessage chatMessage) {
        log.info("chatMessage : {}", chatMessage.getMessage());
        log.info("chatType : {}", chatMessage.getType());
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Long.class));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));

        Long roomId = chatMessage.getRoomId();
        List<ChatMessage> chatMessages = opsHashOperationMessage.get(CHAT_MESSAGE, roomId);
        if (chatMessages.equals(null)) {
            chatMessages = new ArrayList<>();
        }
        chatMessages.add(chatMessage);

        opsHashOperationMessage.put(CHAT_MESSAGE, roomId, chatMessages);

        return chatMessage;
    }

    public void delete(Long roomId) {
        opsHashOperationMessage.delete(CHAT_MESSAGE, roomId);
    }
}

