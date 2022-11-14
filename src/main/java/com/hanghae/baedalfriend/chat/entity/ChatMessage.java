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

public class ChatMessage extends Timestamped implements Serializable {


    //메세지 타입: 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK, QUIT
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


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, Member member) {
        this.type = chatMessageRequestDto.getType();
        this.message = chatMessageRequestDto.getMessage();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.member = member;


    }
}