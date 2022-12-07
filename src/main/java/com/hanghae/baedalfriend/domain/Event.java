package com.hanghae.baedalfriend.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //제목
    @Column(nullable = false)
    private String title;

    //내용
    @Column(nullable = false)
    private String content;

    @Column
    private String imageUrl;
    //멤버(관리자)
    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    public void update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}