package com.hanghae.baedalfriend.chat.entity;

import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import lombok.*;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String roomnum;


    @Column(nullable = false)
    private String writer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;




    public ChatRoom(Member member, String title, String roomnum) {
        this.writer = member.getNickname();
        this.roomnum= roomnum;
        this.title = title;
    }



}