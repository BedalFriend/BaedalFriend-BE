package com.hanghae.baedalfriend.chat.controller;



import com.amazonaws.Response;
import com.hanghae.baedalfriend.chat.dto.ChatRoomResponseDto;
import com.hanghae.baedalfriend.chat.dto.request.ChatRoomRequestDto;
import com.hanghae.baedalfriend.chat.repository.ChatRoomRepository;
import com.hanghae.baedalfriend.chat.service.ChatRoomService;
import com.hanghae.baedalfriend.domain.UserDetailsImpl;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/v1/chat")
public class ChatRoomController {


    private final ChatRoomService chatRoomService;

    // 채팅방 입장
    @PostMapping("/enter/{roomId}")
    public ResponseDto<?> enterRoom(@PathVariable Long roomId, HttpServletRequest request) {
        return chatRoomService.enterRoom(roomId, request);
    }


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