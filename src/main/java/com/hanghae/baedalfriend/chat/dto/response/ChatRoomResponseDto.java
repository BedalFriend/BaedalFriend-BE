package com.hanghae.baedalfriend.chat.dto.response;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private List<ChatRoomMember> chatRoomMembers;
    private List<ChatMessage> chatMessages;
    private String title;
    private Post post;
}