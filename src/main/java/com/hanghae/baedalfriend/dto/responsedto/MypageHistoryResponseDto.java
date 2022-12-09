package com.hanghae.baedalfriend.dto.responsedto;

import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Builder
public class MypageHistoryResponseDto {
    private List<ChatRoomMember> chatRoom;
    private List<ChatRoomMember> chatRoomMembers;
}
