package com.hanghae.baedalfriend.chat.configuration.handler;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;

import com.hanghae.baedalfriend.chat.repository.ChatRoomRepository;
import com.hanghae.baedalfriend.chat.service.ChatRoomService;
import com.hanghae.baedalfriend.chat.service.ChatService;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {


        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


        if (StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("accessor = " + accessor);


            String accessToken = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7);
            String refreshToken = accessor.getFirstNativeHeader("Refresh_Token");

            System.out.println("refreshToken = " + refreshToken);
            System.out.println("accessToken = " + accessToken);

            tokenProvider.validateToken(accessToken);
            tokenProvider.validateToken(refreshToken);

            log.info("Authorization validity is {}", tokenProvider.validateToken(accessToken));
            log.info("RefreshToken validity is {}", tokenProvider.validateToken(refreshToken));

        }

//        //initial connection은 되어 있고 메세지 주고 받기 전 채팅방을 구독하는 상태라면?
//        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
//
//
//            String destination = Optional.ofNullable((String) message.getHeaders().get("simpDestination"))
//                    .orElse("Invalid RoomId");
//            log.info("message destination={}", destination);
//            log.info("message header info {}", message.getHeaders());
//
//            String roomId = chatRoomService.getRoomId(destination);
//            System.out.println("roomId roomId roomId roomId roomId roomId roomId roomId roomId roomId roomId roomId = " + roomId);
//
//            //Client마다 SessionID 생성 => ChatRoomId와 mapping
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            log.info("session Id is {}", sessionId);
//            chatRoomRepository.setUserEnterInfo(sessionId, roomId);
//
//            //채팅방 인원수 +1
//            chatRoomRepository.plusUserCount(roomId);
//
//            Member member = tokenProvider.getMemberFromAuthentication();
//
//
//            log.info("member INFO {}", member);
//            System.out.println("message message message message message messade message  = " + message);
//            System.out.println(" simpleUser= " + message.getHeaders()
//                    .get("simpUser"));
//            Long chatroomId = Long.valueOf(roomId);
//
//
//            chatService.sendChatMessage(
//                    ChatMessage.builder()
//                            .type(ChatMessage.MessageType.ENTER)
//                            .roomId(chatroomId)
//                            .member(member)
//                            .build()
//
//            );
//
//
//            log.info("SUBSCRIBED {}, {}", member.getNickname(), roomId);
//
//        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
//            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//
//            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
//            Long chatroomId = Long.valueOf(roomId);
//            Member member = tokenProvider.getMemberFromAuthentication();
//            // 채팅방의 인원수를 -1한다.
//            chatRoomRepository.minusUserCount(roomId);
//            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.
//
//            chatService.sendChatMessage(ChatMessage.builder()
//                    .type(ChatMessage.MessageType.QUIT)
//                    .roomId(chatroomId)
//                    .member(member)
//                    .build()
//            );
//            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
//            chatRoomRepository.removeUserEnterInfo(sessionId);
//            log.info("DISCONNECTED {}, {}", sessionId, roomId);
//        }


        //return ChannelInterceptor.super.preSend(message, channel);
        return message;
    }


}
