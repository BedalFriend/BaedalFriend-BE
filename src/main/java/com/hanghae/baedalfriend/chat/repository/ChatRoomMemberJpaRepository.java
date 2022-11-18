package com.hanghae.baedalfriend.chat.repository;

import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberJpaRepository extends JpaRepository<ChatRoomMember, Long> {
    void deleteByMember(Member member);

    List<ChatRoomMember> findAllByChatRoom(ChatRoom chatRoom);


    Optional<ChatRoomMember> findByMember(Member member);
}