package com.hanghae.baedalfriend.chat.service;


import com.hanghae.baedalfriend.chat.dto.response.ChatRoomResponseDto;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.chat.repository.ChatMessageJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberJpaRepository;
import com.hanghae.baedalfriend.chat.repository.ChatRoomJpaRepository;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, ChatRoom> hashOpsChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장

    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatService chatService;


    @PostConstruct
    private void init() {
        hashOpsChatRoom = redisTemplate.opsForHash();
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

    public void createChatRoom(Post post,
                               HttpServletRequest request) {


        Member member = validateMember(request);
        String founder = member.getNickname();


        ChatRoom chatRoom = new ChatRoom(founder, post);

        chatRoomJpaRepository.save(chatRoom);

        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getId(), chatRoom);
    }


    public ResponseDto<?> enterRoom(Long roomId, HttpServletRequest request) {

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

        ChatRoom chatRoom = chatRoomJpaRepository.findById(roomId).orElseThrow(
                () -> new NullPointerException("해당하는 채팅방이 없습니다.")
        );
        List<ChatRoomMember> isnull = chatRoomMemberJpaRepository.findByMember(member);
        System.out.println(isnull);

        if (isnull.size() == 0) {
            ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                    .chatRoom(chatRoom)
                    .member(member)
                    .build();
            chatRoomMemberJpaRepository.save(chatRoomMember);
            return ResponseDto.success("채팅방입장");

        } else {
            return ResponseDto.fail("채팅방입장 불가", "채팅방입장 불가");

        }
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
        ChatRoom chatRoom = chatRoomJpaRepository.findById(roomId).orElseThrow(
                () -> new NullPointerException("해당하는 채팅방이 없습니다.")
        );


        // 나간 유저를 채팅방 리스트에서 제거
        chatRoomMemberJpaRepository.deleteByMember(member);
        // 현재 채팅룸에 한명이라도 남아있다면 퇴장메시지 전송
        if (chatRoomMemberJpaRepository.findAllByChatRoom(chatRoom).size() == 0) {
            chatService.sendChatMessage(
                    ChatMessage.builder()
                            .type(ChatMessage.MessageType.QUIT)
                            .roomId(roomId)
                            .sender(member.getNickname())
                            .member(member)
                            .build()
            );
            return ResponseDto.success("퇴장메시지 전송 성공");

        } else {
            return ResponseDto.success("채팅방안 사람이 없음");

        }

    }

    //채팅방 하나 불러오기
    public ResponseDto<?> findRoom(Long roomId, HttpServletRequest request) {

        Member member = validateMember(request);

        ChatRoom chatRoom = chatRoomJpaRepository.findById(roomId).orElseThrow(
                () -> new NullPointerException("해당하는 채팅방이 없습니다.")
        );

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


        List<ChatRoomMember> chatRoomMembers = chatRoomMemberJpaRepository.findAllByChatRoom(chatRoom);
        List<ChatMessage> chatMessages = chatMessageJpaRepository.findAllByRoomId(roomId);
        Post post = postRepository.findById(roomId).get();


        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto
                .builder()
                .chatRoomMembers(chatRoomMembers)
                .chatMessages(chatMessages)
                .post(post)
                .build();
        return ResponseDto.success(chatRoomResponseDto);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh_token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
