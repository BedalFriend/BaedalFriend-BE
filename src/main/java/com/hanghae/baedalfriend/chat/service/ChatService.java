package com.hanghae.baedalfriend.chat.service;


import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.repository.ChatMessageJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatMessageRepository;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;


    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final MemberRepository memberRepository;

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
    public void save(ChatMessage chatMessage) {

        Member member=memberRepository.findByNickname(chatMessage.getSender()).get();
        ChatMessage message = new ChatMessage();
        message.setType(chatMessage.getType());
        message.setMessage(chatMessage.getMessage());
        message.setRoomId(chatMessage.getRoomId());
        message.setSender(chatMessage.getSender());
        message.setMember(member);
        message.setCreatedAt(chatMessage.getCreatedAt());


        chatMessageRepository.save(message);
        chatMessageJpaRepository.save(message);
        sendChatMessage(message);


    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}