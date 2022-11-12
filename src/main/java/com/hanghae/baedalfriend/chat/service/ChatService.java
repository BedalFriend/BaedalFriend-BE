package com.hanghae.baedalfriend.chat.service;


import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.repository.ChatMessageRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    // 채팅방에 발행되는 메시지를 처리할 Listener
    // 1:N 방식으로 topic처리 Listener

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private RedisSubscriber redisSubscriber;


    private final ChatMessageRepository chatMessageRepository;



    // 메시지 전송
    public void sendChatMessage(ChatMessage chatMessage) {

        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            log.info("Message Type is ENTER");
            chatMessage.setMessage("입장");
        }else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage("퇴장");
        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
        redisSubscriber.sendMessage(chatMessage);

    }

    // 메시지 저장
    public void save(ChatMessage chatRoomMessage) {
        ChatMessage message = new ChatMessage();
        message.setType(chatRoomMessage.getType());
        message.setMessage(chatRoomMessage.getMessage());
        message.setMessage(chatRoomMessage.getMessage());
        message.setSender(chatRoomMessage.getSender());

        chatMessageRepository.save(message);

    }

}
