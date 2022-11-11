package com.hanghae.baedalfriend.chat.service;


import com.hanghae.baedalfriend.chat.dto.ChatRoomResponseDto;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomRepository;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.UserDetailsImpl;
import com.hanghae.baedalfriend.dto.requestdto.PostRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Long> hashOperationsEnterInfo;
    public static final String ENTER_INFO = "ENTER_INFO";  // 채팅룸에 입장한 USER의 sessionId와 채팅룸 id를 맵핑한 정보
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatService chatService;

    private final TokenProvider tokenProvider;


    @PostConstruct
    private void init() {
        hashOperationsEnterInfo = redisTemplate.opsForHash();
    }

    // 유저가 입장한 채팅방 ID와 유저 세션 ID 매핑 정보 저장
    // 22번째 줄에서 이미 중요 정보 매핑
    public void setUserEnterInfo(String sessionId, Long roomId) {
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class));

        hashOperationsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public Long getUserEnterRoomId(String sessionId) {
        return hashOperationsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션 정보와 매핑된 채팅방 ID삭제
    // 실시간으로 유저가 보는 방(1) : 유저가 매핑 되어 있는 방 (1)
    public void removeUserEnterInfo(String sessionId) {
        hashOperationsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // destination 정보에서 roomId 추출 (String)
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else
            return "";
    }

    // 채팅방 생성

    public void createChatRoom(PostRequestDto requestDto,
                               HttpServletRequest request) {
        Member writer = validateMember(request);


            String title=requestDto.getRoomTitle();
            ChatRoom chatRoom = new ChatRoom(writer,title);
            chatRoomRepository.save(chatRoom);



    }

    //채팅방 하나 불러오기
    public ResponseDto<?> findRoom(Long roomId, HttpServletRequest request) {


        Member member = validateMember(request);



        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }

        if (null == request.getHeader("Refresh_token")) {
            return ResponseDto.fail("INVALID_TOKEN",
                    "Token이 유효하지 않습니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("INVALID_TOKEN",
                    "Token이 유효하지 않습니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new NullPointerException("해당하는 채팅방이 없습니다.")
        );
        List<ChatRoomMember> chatRoomMembers=chatRoomMemberRepository.findAllByChatRoom(chatRoom);

        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto
                .builder()
                .chatRoomMembers(chatRoomMembers)
                .build();

        return ResponseDto.success(chatRoomResponseDto);
    }

    public ResponseDto<?> enterRoom(Long roomId, HttpServletRequest request) {
        Member member = validateMember(request);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new NullPointerException("해당하는 채팅방이 없습니다.")
        );
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatRoomMemberRepository.save(chatRoomMember);
        return ResponseDto.success("채팅방입장");
    }

    // 특정 채팅방 나가기
    // 나간 유저 : 나간 액션 처리
    // 방에 있는 유저 : [xx]님이 나가셨습니다.
    @Transactional
    public ResponseDto<?> leaveChatRoom(Long roomId, HttpServletRequest request) {

        Member member = validateMember(request);



        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }

        if (null == request.getHeader("Refresh_token")) {
            return ResponseDto.fail("INVALID_TOKEN",
                    "Token이 유효하지 않습니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("INVALID_TOKEN",
                    "Token이 유효하지 않습니다.");
        }


        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new NullPointerException("해당하는 채팅방이 없습니다.")
        );


        // 나간 유저를 채팅방 리스트에서 제거
        chatRoomMemberRepository.deleteByMember(member);
        // 현재 채팅룸에 한명이라도 남아있다면 퇴장메시지 전송
        if (chatRoomMemberRepository.findAllByChatRoom(chatRoom) != null) {
            chatService.sendChatMessage(
                    ChatMessage.builder()
                            .type(ChatMessage.MessageType.QUIT)
                            .roomId(roomId)
                            .memberId(member.getId())
                            .build());
            return ResponseDto.success("퇴장메시지전송성공");

        } else {
            return ResponseDto.success("채팅방안 사람이 없음");

        }

    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
