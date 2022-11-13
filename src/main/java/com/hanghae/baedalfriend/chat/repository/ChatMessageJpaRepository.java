package com.hanghae.baedalfriend.chat.repository;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import com.hanghae.baedalfriend.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {

}
