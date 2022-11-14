package com.hanghae.baedalfriend.chat.entity;


import com.hanghae.baedalfriend.domain.Post;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    @Column(nullable = false)
    private String founder;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    public ChatRoom(String founder,Post post) {
        this.founder =founder;
        this.post = post;
    }
}