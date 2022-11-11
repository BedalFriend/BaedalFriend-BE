package com.hanghae.baedalfriend.chat.dto;

import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private Long chatRoomId;
    private Long memberId;

    private List<ChatRoomMember> chatRoomMembers;

    public ChatRoomResponseDto(ChatRoom chatRoom, Member member, List<ChatRoomMember> chatRoomMembers) {
        this.chatRoomId = chatRoom.getRoomId();
        this.memberId = member.getId();
        this.chatRoomMembers = chatRoomMembers;
    }


}
