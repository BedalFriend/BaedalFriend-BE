package com.hanghae.baedalfriend.chat.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.baedalfriend.domain.Post;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomMember> memberList;

    public ChatRoom(String founder,Post post) {
        this.founder =founder;
        this.post = post;
    }
}