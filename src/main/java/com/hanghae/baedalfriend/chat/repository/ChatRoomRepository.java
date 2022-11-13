package com.hanghae.baedalfriend.chat.repository;

import com.hanghae.baedalfriend.chat.entity.ChatRoom;
import com.hanghae.baedalfriend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByRoomnum (String roomId);


}
