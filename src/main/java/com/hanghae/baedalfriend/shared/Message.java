package com.hanghae.baedalfriend.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class Message {
    private boolean success;
    private Object data;
    private Error error;

    public static Message success(Object data){
        return new Message(true,data,null);
    }
    public static Message fail(String code,String message){
        return new Message(false,null,new Error(code, message));
    }
    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;
    }

}
