package com.hanghae.baedalfriend.chat.controller;

import com.hanghae.baedalfriend.chat.service.ChatRoomService;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//
import javax.servlet.http.HttpServletRequest;
//

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/chat")
public class ChatRoomController {


    private final ChatRoomService chatRoomService;





    //채팅방 나가기
    @DeleteMapping("/channel/{roomId}")
    @ResponseBody
    public ResponseDto<?> deleteChatRoom(@PathVariable Long roomId, HttpServletRequest request){
        return chatRoomService.leaveChatRoom(roomId,request);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseDto<?> roomInfo(@PathVariable Long roomId, HttpServletRequest request) {
        return chatRoomService.findRoom(roomId, request);
    }



}
