//package com.hanghae.baedalfriend.controller;
//
//import com.hanghae.baedalfriend.dto.PhotoDto;
//import com.hanghae.baedalfriend.dto.requestdto.EventRequestDto;
//import com.hanghae.baedalfriend.dto.requestdto.EventUpRequestDto;
//import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
//import com.hanghae.baedalfriend.service.EventService;
////import com.hanghae.baedalfriend.service.S3Service;
////import com.hanghae.baedalfriend.service.S3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@RequiredArgsConstructor
//@RequestMapping("/v1")
//@RestController
//public class EventController {
//
//    private final EventService eventService;
//    private final S3Service s3Service;
//    //공지사항(이벤트) 등록
//    @PostMapping("/events")
//    public ResponseDto<?> createEvent(@RequestPart(value = "event") EventRequestDto requestDto,
//                                      @RequestPart(value = "imgUrl", required = false) MultipartFile multipartFile,
//                                      HttpServletRequest request) {
//        List<PhotoDto> photoDtos = s3Service.uploadImage(multipartFile);
//        return eventService.createEvent(requestDto, photoDtos, request);
//    }
//
//    //공지사항(이벤트) 전체 조회
//    @GetMapping("/events")
//    public ResponseDto<?> getAllEvents(Pageable pageable) {
//        return eventService.getAllEvents(pageable);
//    }
//
//    //공지사항(이벤트) 1개 조회
//    @GetMapping("/events/{eventId}")
//    public ResponseDto<?> getEvent(@PathVariable Long eventId) {
//        return eventService.getEvent(eventId);
//    }
//
//    //공지사항(이벤트) 수정
//    @PutMapping("/events/{eventId}")
//    public ResponseDto<?> updateEvent(@PathVariable Long eventId,
//                                      @RequestPart("event") EventUpRequestDto requestDto,
//                                      @RequestPart("imgUrl") MultipartFile multipartFile,
//                                      HttpServletRequest request) {
//        List<PhotoDto> photoDtos  = s3Service.uploadImage(multipartFile);
//        return eventService.updateEvent(eventId, requestDto, photoDtos, request);
//    }
//
//    //공지사항(이벤트) 삭제
//    @DeleteMapping("/events/{eventId}")
//    public ResponseDto<?> deleteEvent(@PathVariable Long eventId, HttpServletRequest request) {
//        return eventService.deleteEvent(eventId, request);
//    }
//}
//
//
