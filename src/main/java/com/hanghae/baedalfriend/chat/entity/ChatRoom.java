package com.hanghae.baedalfriend.chat.entity;

import com.hanghae.baedalfriend.domain.Member;
import lombok.*;


import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatRoom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long roomId;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writer;




    public ChatRoom(Member member, String title) {
        this.writer = member.getNickname();
        this.title = title;
    }


}