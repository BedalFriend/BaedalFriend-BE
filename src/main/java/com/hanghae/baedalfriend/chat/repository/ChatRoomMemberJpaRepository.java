package com.hanghae.baedalfriend.chat.repository;

import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import com.hanghae.baedalfriend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberJpaRepository extends JpaRepository<ChatRoomMember, Long> {
    void deleteByMember(Member member);
    void deleteAllByChatRoom(ChatRoom chatRoom);

    List<ChatRoomMember> findAllByChatRoom(ChatRoom chatRoom);

    List<ChatRoomMember> findByMember(Member member);

    void deleteByMemberId(Long memberId);

    List<ChatRoomMember> findAllByMemberId(Long memberId);

    ChatRoomMember findByMemberId(Long memberId);

    void deleteByChatRoom(ChatRoom chatRoom);
}