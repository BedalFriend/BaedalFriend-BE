package com.hanghae.baedalfriend.chat.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hanghae.baedalfriend.chat.dto.request.ChatMessageRequestDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
@Builder
public class ChatMessage implements Serializable {



    //메세지 타입: 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK ,QUIT
    }
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long messageId;

    @Column(nullable = false)
    private MessageType type;// 메세지 타입

    //    @Column(nullable = false)
    @Column(nullable = false)
    private String title;   //message 속해있는 방 Id
    @Column(nullable = false)
    private String message; // 메세지
    @Column(nullable = false)
    private String createdAt; // 생성일자

//    @Column(nullable = false)
//    private long memberId; // 보낸사람

    @Column(nullable = false)
    private String sender; // 보낸사람







    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        this.type = chatMessageRequestDto.getType();
        this.title = chatMessageRequestDto.getTitle();
        this.message = chatMessageRequestDto.getMessage();
        this.createdAt = chatMessageRequestDto.getCreatedAt();
        this.sender =chatMessageRequestDto.getSender();
    }
}