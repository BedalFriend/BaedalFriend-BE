package com.hanghae.baedalfriend.chat.entity;

import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomMember extends Timestamped {




    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "chatRoomId")
    @ManyToOne
    private ChatRoom chatRoom;

    @JoinColumn(name = "memebrId")
    @ManyToOne
    private Member member;



    @Builder
    public ChatRoomMember (ChatRoom chatRoom, Member member) {
        this.member = member;
        this.chatRoom = chatRoom;

    }
}