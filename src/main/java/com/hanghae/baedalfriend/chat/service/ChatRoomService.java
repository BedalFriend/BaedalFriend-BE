//package com.hanghae.baedalfriend.chat.service;
//
//
//import com.hanghae.baedalfriend.chat.dto.response.ChatRoomResponseDto;
//import com.hanghae.baedalfriend.chat.entity.ChatMessage;
//import com.hanghae.baedalfriend.chat.entity.ChatRoom;
//import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
//import com.hanghae.baedalfriend.chat.repository.ChatMessageRepository;
//import com.hanghae.baedalfriend.chat.repository.ChatRoomMemberRepository;
//import com.hanghae.baedalfriend.chat.repository.ChatRoomJpaRepository;
//import com.hanghae.baedalfriend.domain.Member;
//import com.hanghae.baedalfriend.domain.Post;
//import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
//import com.hanghae.baedalfriend.jwt.TokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.transaction.Transactional;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class ChatRoomService {
//    private final RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, Long> hashOperationsEnterInfo;
//    public static final String ENTER_INFO = "ENTER_INFO";  // 채팅룸에 입장한 USER의 sessionId와 채팅룸 id를 맵핑한 정보
//    private final ChatRoomJpaRepository chatRoomRepository;
//    private final ChatMessageRepository chatMessageRepository;
//    //    private final ChatRoomRepository roomRepository;
//    private final ChatRoomMemberRepository chatRoomMemberRepository;
//    private final ChatService chatService;
//    private final TokenProvider tokenProvider;
//    @Resource(name = "redisTemplate")
//    private HashOperations<String, Long, ChatRoom> hashOpsChatRoom;
//    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
//
//    @PostConstruct
//    private void init() {
//        hashOperationsEnterInfo = redisTemplate.opsForHash();
//        hashOpsChatRoom = redisTemplate.opsForHash();
//    }
//
//
//    // destination 정보에서 roomId 추출 (String)
//    public String getRoomId(String destination) {
//        int lastIndex = destination.lastIndexOf('/');
//        if (lastIndex != -1) {
//            return destination.substring(lastIndex + 1);
//        } else
//            return "";
//    }
//
//    // 채팅방 생성
//
//    public void createChatRoom(Post post,
//                               HttpServletRequest request) {
//
//
//        Member member = validateMember(request);
//        String founder = member.getNickname();
//
//
//        ChatRoom chatRoom = new ChatRoom(founder, post);
//
//        chatRoomRepository.save(chatRoom);
//
//        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getId(), chatRoom);
//    }
//
//    //채팅방 하나 불러오기
//    public ResponseDto<?> findRoom(String roomId, HttpServletRequest request) {
//        Member member = validateMember(request);
//
//        Optional<ChatRoom> chatRoom = chatRoomRepository.findByRoomnum(roomId)
//                chatRoom.get().get
//
//        if (null == member) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
//        }
//
//        if (null == request.getHeader("Refresh_token")) {
//            return ResponseDto.fail("INVALID_TOKEN",
//                    "Token이 유효하지 않습니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("INVALID_TOKEN",
//                    "Token이 유효하지 않습니다.");
//        }
//
//
//        ChatRoomMember chatRoomMembers = chatRoomMemberRepository.findAllByChatRoom(room);
//        Object chatMessages = chatMessageRepository.findAllMessage(roomId, member.getNickname());
//
//        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto
//                .builder()
//                .chatRoomMembers(chatRoomMembers)
//                .chatMessages(chatMessages)
//                .build();
//        return ResponseDto.success(chatRoomResponseDto);
//    }
//
//    public ResponseDto<?> enterRoom(Long roomId, HttpServletRequest request) {
//        Member member = validateMember(request);
//        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
//                () -> new NullPointerException("해당하는 채팅방이 없습니다.")
//        );
//        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
//                .chatRoom(chatRoom)
//                .member(member)
//                .build();
//        chatRoomMemberRepository.save(chatRoomMember);
//        return ResponseDto.success("채팅방입장");
//    }
//
//    // 특정 채팅방 나가기
//    // 나간 유저 : 나간 액션 처리
//    // 방에 있는 유저 : [xx]님이 나가셨습니다.
//    @Transactional
//    public ResponseDto<?> leaveChatRoom(Long roomId, HttpServletRequest request) {
//
//        Member member = validateMember(request);
//
//        if (null == member) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
//        }
//
//        if (null == request.getHeader("Refresh_token")) {
//            return ResponseDto.fail("INVALID_TOKEN",
//                    "Token이 유효하지 않습니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("INVALID_TOKEN",
//                    "Token이 유효하지 않습니다.");
//        }
//
//        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
//        ChatRoom room = chatRoom.get();
//
//        // 나간 유저를 채팅방 리스트에서 제거
//        chatRoomMemberRepository.deleteByMember(member);
//        // 현재 채팅룸에 한명이라도 남아있다면 퇴장메시지 전송
//        if (chatRoomMemberRepository.findAllByChatRoom(room) != null) {
//            chatService.sendChatMessage(
//                    ChatMessage.builder()
//                            .type(ChatMessage.MessageType.QUIT)
////                            .title(chatRoom.getTitle())
//                            .sender(member.getNickname())
//                            .build()
//            );
//            return ResponseDto.success("퇴장메시지전송성공");
//
//        } else {
//            return ResponseDto.success("채팅방안 사람이 없음");
//
//        }
//
//    }
//
//    @Transactional
//    public Member validateMember(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("Refresh_token"))) {
//            return null;
//        }
//        return tokenProvider.getMemberFromAuthentication();
//    }
//
//
//}
