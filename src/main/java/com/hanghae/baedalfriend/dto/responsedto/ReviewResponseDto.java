package com.hanghae.baedalfriend.dto.responsedto;

import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private List<ChatRoomMember> chatRoomMemberList;


    public ReviewResponseDto(List<ChatRoomMember> chatRoomMemberList){
        this.chatRoomMemberList=chatRoomMemberList;

    }
}
