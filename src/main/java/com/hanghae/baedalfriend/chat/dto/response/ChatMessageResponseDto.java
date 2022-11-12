package com.hanghae.baedalfriend.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class ChatMessageResponseDto {


    private String sender;
    private  String message;
    private String title;



    public ChatMessageResponseDto(String sender,String message,String title){

        this.sender = sender;
        this.title =title;
        this.message=message;

    }
}