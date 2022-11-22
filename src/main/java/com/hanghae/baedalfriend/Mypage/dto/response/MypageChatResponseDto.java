package com.hanghae.baedalfriend.Mypage.dto.response;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Builder
public class MypageChatResponseDto {
    private List<ChatRoomMember> chatRoomMembers;
    private List<ChatMessage> chatMessages;
    private List<ChatRoom> chatRooms;
}
