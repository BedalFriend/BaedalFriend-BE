package com.hanghae.baedalfriend.chat.entity;


import com.hanghae.baedalfriend.chat.dto.request.ChatMessageRequestDto;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Timestamped;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class ChatMessage implements Serializable  {


    //메세지 타입: 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK, EXIT
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private MessageType type;// 메세지 타입

    @Column
    private String message; // 메세지


    @Column(nullable = false)
    private Long roomId; // 채팅방번호

    @Column(nullable = false)
    private String sender; //보내는 사람

    @JoinColumn(name = "member_Id", nullable = false)
    @ManyToOne
    private Member member;

    private String createdAt;




    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        this.type = chatMessageRequestDto.getType();
        this.message = chatMessageRequestDto.getMessage();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.sender = chatMessageRequestDto.getSender();


    }
}