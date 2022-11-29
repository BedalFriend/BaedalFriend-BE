package com.hanghae.baedalfriend.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.domain.Post;
import com.hanghae.baedalfriend.domain.Timestamped;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomMember extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "chatRoom_id")
    @ManyToOne
    private ChatRoom chatRoom;

    @JoinColumn(name = "memebr_id")
    @ManyToOne
    private Member member;

    @ManyToOne
    @JoinColumn(name = "participant_number_ID")
    private ParticipantNumber participantNumber;

    @Builder
    public ChatRoomMember(ChatRoom chatRoom, Member member , ParticipantNumber participantNumber) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.participantNumber =  participantNumber;
    }


}