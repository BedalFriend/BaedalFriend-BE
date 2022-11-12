package com.hanghae.baedalfriend.chat.configuration.handler;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.repository.ChatRoomRepository;
import com.hanghae.baedalfriend.chat.repository.RoomRepository;
import com.hanghae.baedalfriend.chat.service.ChatRoomService;
import com.hanghae.baedalfriend.chat.service.ChatService;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.UserDetailsImpl;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatRoomService chatRoomService;
    private final RoomRepository roomRepository;
    private final ChatService chatService;
//    private final RedisSubscriber redisSubscriber;


    /*
    Invoked before the Message is actually send to the channel.
    This allows for modification of the message if necessary.
    If this method returns null, then the actual send the invocation will not occur
    */

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //Create an instance from the payload and headers of the given Message.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        /*
         * 1. client -> server
         * 2. webSocket connect request
         * 3. if 'CONNECT' -> initial connection
         * */
        //if (StompCommand.CONNECT == accessor.getCommand()){
        if (StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("accessor = " + accessor);
            //intial connection => Token 유효성 검사
            //Access Token invalid => reissue
            //Refresh Token invalid => login again

            String accessToken = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7);
            String refreshToken = accessor.getFirstNativeHeader("Refresh_Token");
            System.out.println("refreshToken = " + refreshToken);
            System.out.println("accessToken = " + accessToken);
            tokenProvider.validateToken(accessToken);
            tokenProvider.validateToken(refreshToken);
            log.info("Authorization validity is {}",tokenProvider.validateToken(accessToken));
            log.info("RefreshToken validity is {}",tokenProvider.validateToken(refreshToken));
//            accessor.setUser(new User(accessor.getLogin());
        }

        //initial connection은 되어 있고 메세지 주고 받기 전 채팅방을 구독하는 상태라면?
//        else if (StompCommand.SUBSCRIBE == accessor.getCommand()){
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()){

            //header destination = /sub/chats/rooms/{roomId}
            String destination = Optional.ofNullable((String)message.getHeaders().get("simpDestination"))
                    .orElse("Invalid RoomId");
            log.info("message destination={}", destination);
            log.info("message header info {}", message.getHeaders());

            //roomId get
            String roomId = chatRoomService.getRoomId(destination);
            System.out.println("roomId 111111111111111111111111111111111111111111= " + roomId);
            //Client마다 SessionID 생성 => ChatRoomId와 mapping
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            log.info("session Id is {}", sessionId);
            roomRepository.setUserEnterInfo(sessionId, roomId);

            //채팅방 인원수 +1
            roomRepository.plusUserCount(roomId);
//            String name = Optional.ofNullable((Principal) message.getHeaders()
//                    .get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            Member member = tokenProvider.getMemberFromAuthentication();

            //이름 그냥 넣어주기 로그인 정보에서
//            Member member = tokenProvider.getMemberFromAuthentication();
            log.info("member INFO {}",member);
            System.out.println("message12312412412421412412421 = " + message);
            System.out.println(" simpleUser= " +message.getHeaders()
                    .get("simpUser"));
//            System.out.println("name111111111111111111111111111111111111 = " + member.getNickName());

            chatService.sendChatMessage(
                    ChatMessage.builder()
                            .type(ChatMessage.MessageType.ENTER)
                            .roomId(roomId)
                            .sender(member.getNickname())
                            .build()

            );


            log.info("SUBSCRIBED {}, {}", member.getNickname(), roomId);
        }




        //return ChannelInterceptor.super.preSend(message, channel);
        return message;
    }





}
