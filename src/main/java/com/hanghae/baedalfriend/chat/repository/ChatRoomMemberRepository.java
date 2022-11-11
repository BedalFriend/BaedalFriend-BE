package com.hanghae.baedalfriend.chat.repository;


import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    ChatRoomMember deleteByMemberId(Long id);

    List<ChatRoomMember> findByChatRoomId(Long id);

    ChatRoomMember findByChatRoomIdAndMemberId(Long id, Long member_id);

}
