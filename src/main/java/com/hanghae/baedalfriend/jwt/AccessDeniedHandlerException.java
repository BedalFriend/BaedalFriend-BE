package com.hanghae.baedalfriend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerException implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        ResponseDto.fail("BAD_REQUEST", "이 작업은 로그인이 필요합니다.")
                )
        );
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}