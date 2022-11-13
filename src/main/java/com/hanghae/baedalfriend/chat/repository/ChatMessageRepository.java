package com.hanghae.baedalfriend.chat.repository;


import com.hanghae.baedalfriend.chat.dto.response.ChatMessageResponseDto;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Repository
public class ChatMessageRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatMessage> hashOpsChatMessage;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, List<ChatMessage>> opsHashChatMessages;

    @PostConstruct
    private void init() {
        opsHashChatMessages = redisTemplate.opsForHash();
    }

    public void save(ChatMessage chatMessage) {
        System.out.println("chatMessage is + " + chatMessage);
        hashOpsChatMessage.put(chatMessage.getRoomId(), chatMessage.getSender(),chatMessage);

    }

//    public void delete(ChatMessage chatMessage) {
//        hashOpsChatMessage.delete(chatMessage.getRoomId(), chatMessage.getSender());
//    }

    public ChatMessage findAllMessage(String roomId,String sender) {
        // Deserialize = 예시) MultiPartFile --> File

        return hashOpsChatMessage.get(roomId,sender);
    }
}

