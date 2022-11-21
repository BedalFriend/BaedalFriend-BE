package com.hanghae.baedalfriend.service;

import com.hanghae.baedalfriend.domain.Event;
import com.hanghae.baedalfriend.domain.Member;
import com.hanghae.baedalfriend.dto.PhotoDto;
import com.hanghae.baedalfriend.dto.requestdto.EventRequestDto;
import com.hanghae.baedalfriend.dto.requestdto.EventUpRequestDto;
import com.hanghae.baedalfriend.dto.responsedto.EventResponseDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hanghae.baedalfriend.shared.Authority.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final TokenProvider tokenProvider;
    private final S3Service s3Service;

    @Transactional
    public ResponseDto<?> createEvent(EventRequestDto requestDto, List<PhotoDto> photoDtos, HttpServletRequest request) {

        //현재 토큰이 유효한지?, 관리자 권한인지 확인
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND","사용자를 찾을 수 없습니다.");
        }else if (member.getRole()!= ROLE_ADMIN) {
            return ResponseDto.fail("MEMBER_NOT_ALLOWED", "관리자가 아닙니다.");
        }

        //게시글 등록(save)
//        String imageUrl = s3Service.uploadImage(multipartFile).toString();
//        System.out.println("imageUrl : " + imageUrl);
        Event event = Event.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrl(photoDtos.get(0).getPath())
                .member(member)
                .build();
        eventRepository.save(event);
        //"이벤트성공"
        return ResponseDto.success("이벤트 성공");
    }

    @Transactional
    public ResponseDto<?> getAllEvents(Pageable pageable) {
        Page<Event> eventList = eventRepository.findAll(pageable);
        List<EventResponseDto> eventResponseDtoList = new ArrayList<>();
        for (Event event : eventList) {
            eventResponseDtoList.add(
                    EventResponseDto.builder()
                            .eventId(event.getId())
                            .title(event.getTitle())
                            .content(event.getContent())
                            .imageUrl(event.getImageUrl())
                            .build()
            );
        }

        return ResponseDto.success(eventResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> getEvent(Long eventId) {
        //eventId가 저장되어있는지 확인
        Event event = isPresentEvent(eventId);
        if (null == event) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다."); //채우기
        }

        return ResponseDto.success(
                EventResponseDto.builder()
                        .eventId(event.getId())
                        .title(event.getTitle())
                        .content(event.getContent())
                        .imageUrl(event.getImageUrl())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> updateEvent(Long eventId, EventUpRequestDto requestDto, List<PhotoDto> photoDtos, HttpServletRequest request) {
        //현재 토큰이 유효한지?, 관리자 권한인지 확인
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND","사용자를 찾을 수 없습니다.");
        }else if (member.getRole()!= ROLE_ADMIN) {
            return ResponseDto.fail("MEMBER_NOT_ALLOWED", "관리자가 아닙니다.");
        }
        //작성한 게시글이 있는지
        Event event = isPresentEvent(eventId);
        if (null == event) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        //본인이 작성한 게시글이 맞는지 확인
        if (event.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        s3Service.deleteImage(event.getImageUrl());

        event.update(requestDto, photoDtos);
        return ResponseDto.success("수정 성공");
    }

    @Transactional
    public ResponseDto<?> deleteEvent(Long eventId, HttpServletRequest request){
        //현재 토큰이 유효한지?, 관리자 권한인지 확인
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND","사용자를 찾을 수 없습니다.");
        }else if (member.getRole()!= ROLE_ADMIN) {
            return ResponseDto.fail("MEMBER_NOT_ALLOWED", "관리자가 아닙니다.");
        }
        //작성한 게시글이 있는지
        Event event = isPresentEvent(eventId);
        if (null == event) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
        }
        //본인이 작성한 게시글이 맞는지 확인
        if (event.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        //게시글 삭제(delete)
        String imageUrl = event.getImageUrl();
        s3Service.deleteImage(imageUrl);

        eventRepository.deleteById(eventId);
        //"삭제 성공"
        return ResponseDto.success("삭제 성공");
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("refresh_token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public Event isPresentEvent(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        return optionalEvent.orElse(null);
    }
}