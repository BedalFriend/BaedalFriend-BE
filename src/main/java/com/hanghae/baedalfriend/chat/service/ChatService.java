package com.hanghae.baedalfriend.chat.service;


import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.chat.repository.ChatMessageRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberRepository;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    // 채팅방에 발행되는 메시지를 처리할 Listener
    // 1:N 방식으로 topic처리 Listener

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final RedisPublisher redisPublisher;

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    // 메시지 전송
    public void sendChatMessage(ChatMessage chatMessage) {

        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            log.info("Message Type is ENTER");
            chatMessage.setMessage("입장");
        }else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage("퇴장");
        }
        redisPublisher.publish(channelTopic,chatMessage);

    }

    // 알림 저장
    private void saveNotification(ChatMessage chatRoomMessage) {
        ChatMessage message = new ChatMessage();
        message.setType(chatRoomMessage.getType());
        message.setRoomId(chatRoomMessage.getRoomId());
        message.setMemberId(chatRoomMessage.getMemberId());
        message.setMessage(chatRoomMessage.getMessage());
        message.setCreatedAt(chatRoomMessage.getCreatedAt());
        chatMessageRepository.save(message);
    }
    // 메시지 저장
    public void save(ChatMessage chatRoomMessage) {
        ChatMessage message = new ChatMessage();
        message.setType(chatRoomMessage.getType());
        message.setMessage(chatRoomMessage.getMessage());
        message.setRoomId(chatRoomMessage.getRoomId());
        message.setMemberId(chatRoomMessage.getMemberId());
        message.setMessage(chatRoomMessage.getMessage());
        message.setCreatedAt(chatRoomMessage.getCreatedAt());

        chatMessageRepository.save(message);

    }

}
