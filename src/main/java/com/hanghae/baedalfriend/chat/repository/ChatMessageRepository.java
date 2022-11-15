package com.hanghae.baedalfriend.chat.repository;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@RequiredArgsConstructor
@Slf4j
@Repository
public class ChatMessageRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, ChatMessage> hashOpsChatMessage;
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";


    @PostConstruct
    private void init() {
        hashOpsChatMessage = redisTemplate.opsForHash();
    }

    public void save(ChatMessage chatMessage) {
        System.out.println("chatMessage is + " + chatMessage);
        hashOpsChatMessage.put(CHAT_MESSAGE, chatMessage.getId(), chatMessage);

    }


    public Object findAllMessage(Long roomId) {

        return hashOpsChatMessage.get(CHAT_MESSAGE,roomId);
    }
}