package com.hanghae.baedalfriend.chat.configuration.handler;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.service.ChatRoomService;
import com.hanghae.baedalfriend.chat.service.ChatService;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final TokenProvider tokenProvider;
    private final ChatRoomService chatRoomService;

    private final ChatService chatService;


    // Controller 에 가기 전에 이곳을 먼저 들리게 된다. 그것이 인터셉터의 역할.
    //    // HTTP의 Request / Response처럼
    //    // WebSocket은 message와 channel을 갖게된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // accessor를 이용하면 내용에 패킷이 접근할 수 있게된다.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


        /*
         * 1. client -> server
         * 2. webSocket connect request
         * 3. if 'CONNECT' -> initial connection
         * */

        // 접근했을때 COMMAND HEADER의 값을 확인 한다.
        // 만약 CONNECT라면 -> 초기 연결임
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            //intial connection => Token 유효성 검사
            //Access Token invalid => reissue
            //Refresh Token invalid => login again
            String accessToken = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7);
            String refreshToken = accessor.getFirstNativeHeader("Refresh_Token");
            System.out.println("refreshToken = " + refreshToken);
            System.out.println("accessToken = " + accessToken);
            tokenProvider.validateToken(accessToken);
            tokenProvider.validateToken(refreshToken);
            log.info("Authorization validity is {}", tokenProvider.validateToken(accessToken));
            log.info("RefreshToken validity is {}", tokenProvider.validateToken(refreshToken));
        }

        //만약 COMMAND가 SUBSCRIBE 즉 메세지를 주고 받기 전 구독하는 것이라면
        //initial connection은 되어 있고 메세지 주고 받기 전 채팅방을 구독하는 상태라면?
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            // header정보에서 구독 destination 정보를 얻고, roomId를 추출한다.
            // roomId를 URL로 전송해주고 있어 추출 필요.
            // destination은 이렇게 생김 ->/sub/chat/room/{룸 아이디}
            String destination = Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId");
            log.info("message header 정보들={}", message.getHeaders());
            log.info("message destination은={}", destination);
            Long roomId = Long.parseLong(chatRoomService.getRoomId(destination));
            log.info("Long으로 Parsing된 roomId는={} [StompHandler_SUBSCRIBE]", roomId);

            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            // sessionId는 현재 들어와있는 유저를 확인하기 위함이다.
            // sessionId는 정상적으로 들어가고있음
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            //sessionId와 roomId를 맵핑
            chatRoomService.setUserEnterInfo(sessionId, roomId);

            // 구독했다는 것은 처음 입장했다는 것이므로 입장 메시지를 발송한다.
            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
            String token = Optional.ofNullable(accessor.getFirstNativeHeader("token")).orElse("UnknownUser");
            log.info("token={} [StompHandler_SUBSCRIBE]", token);
            Long memebrId = tokenProvider.getMemberFromAuthentication().getId();
            chatService.sendChatMessage(
                    ChatMessage.builder()
                            .type(ChatMessage.MessageType.ENTER)
                            .roomId(roomId)
                            .memberId(memebrId)
                            .build());
            log.info("SUBSCRIBED memebrId {}, roomId {}", memebrId, roomId);
        }
        return message;
    }


}
