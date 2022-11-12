package com.hanghae.baedalfriend.chat.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hanghae.baedalfriend.chat.dto.request.ChatMessageRequestDto;
import lombok.*;

import javax.persistence.Column;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
public class ChatMessage {



    //메세지 타입: 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK ,QUIT
    }
    @Column(nullable = false)

    private MessageType type;// 메세지 타입
    @Column(nullable = false)
    private long roomId; // 방 번호
    @Column(nullable = false)
    private String message; // 메세지
    @Column(nullable = false)
    private String createdAt; // 생성일자

    @Column(nullable = false)
    private long memberId; // 보낸사람

    @Column(nullable = false)
    private String sender; // 보낸사람






    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.message = chatMessageRequestDto.getMessage();
        this.createdAt = chatMessageRequestDto.getCreatedAt();
        this.memberId = chatMessageRequestDto.getMemberId();
        this.sender = chatMessageRequestDto.getSender();
    }
}