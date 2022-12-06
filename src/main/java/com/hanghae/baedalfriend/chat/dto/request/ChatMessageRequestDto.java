package com.hanghae.baedalfriend.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae.baedalfriend.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    private ChatMessage.MessageType type;
    private Long roomId;
    private String message;
    private String sender;
    private String createdAt;
}