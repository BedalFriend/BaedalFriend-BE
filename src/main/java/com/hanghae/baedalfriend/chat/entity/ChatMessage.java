package com.hanghae.baedalfriend.chat.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hanghae.baedalfriend.chat.dto.request.ChatMessageRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class ChatMessage {



    //메세지 타입: 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK ,QUIT
    }
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private MessageType type;// 메세지 타입

    @Column(nullable = false)
    private String message; // 메세지
//    @Column(nullable = false)


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cr_id")
    private ChatRoom chatRoom;


//    @Column(nullable = false)
//    private long memberId; // 보낸사람
    @Column(nullable = false)
    private String sender; // 보낸사람







    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto,ChatRoom chatRoom) {
        this.type = chatMessageRequestDto.getType();
        this.message = chatMessageRequestDto.getMessage();
        this.sender =chatMessageRequestDto.getSender();
        this.chatRoom=chatRoom;


    }
}