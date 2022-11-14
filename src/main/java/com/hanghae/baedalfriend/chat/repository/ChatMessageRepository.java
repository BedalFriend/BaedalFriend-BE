package com.hanghae.baedalfriend.chat.repository;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.domain.Member;
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
    private HashOperations<String, Member, ChatMessage> hashOpsChatMessage;


    @PostConstruct
    private void init() {
        hashOpsChatMessage = redisTemplate.opsForHash();
    }

    public void save(ChatMessage chatMessage) {
        System.out.println("chatMessage is + " + chatMessage);
        String roomId = String.valueOf(chatMessage.getRoomId());
        hashOpsChatMessage.put(roomId, chatMessage.getMember(), chatMessage);

    }


    public Object findAllMessage(String roomId, Member member) {

        return hashOpsChatMessage.get(roomId, member);
    }
}