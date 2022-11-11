package com.hanghae.baedalfriend.chat.dto.request;

import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type;
    private Long roomId;
    private String sender;
    private String message;
    private String createdAt;
    private long memberId;
}
