package com.hanghae.baedalfriend.chat.service;


import com.hanghae.baedalfriend.chat.dto.request.ChatMessageRequestDto;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.repository.ChatMessageJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatMessageRepository;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;


    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    private final TokenProvider tokenProvider;

    // 메시지 전송
    public void sendChatMessage(ChatMessage chatMessage) {

        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            log.info("Message Type is ENTER");
            chatMessage.setMessage("입장");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage("퇴장");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    // 메시지 저장
    @Transactional
    public void save(ChatMessageRequestDto messageRequestDto, HttpServletRequest request) {

        Member member =validateMember(request);

        ChatMessage chatMessage = new ChatMessage(messageRequestDto,member);

        chatMessageJpaRepository.save(chatMessage);
        chatMessageRepository.save(chatMessage);

        sendChatMessage(chatMessage);
    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}