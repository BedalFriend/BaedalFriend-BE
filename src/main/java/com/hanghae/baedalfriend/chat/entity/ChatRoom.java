package com.hanghae.baedalfriend.chat.entity;

import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import lombok.*;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatRoom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String Id;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String roomId;


    @Column(nullable = false)
    private String writer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postId")
    private Post post;




    public ChatRoom(Member member, String title, String roomId) {
        this.writer = member.getNickname();
        this.roomId= roomId;
        this.title = title;
    }


}