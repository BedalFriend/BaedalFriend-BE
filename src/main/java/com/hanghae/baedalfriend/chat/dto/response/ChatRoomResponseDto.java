package com.hanghae.baedalfriend.chat.dto.response;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private ChatRoomMember chatRoomMembers;
    private ChatMessage chatMessages;
}