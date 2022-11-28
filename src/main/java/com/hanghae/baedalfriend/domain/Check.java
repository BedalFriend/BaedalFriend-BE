package com.hanghae.baedalfriend.domain;
import com.hanghae.baedalfriend.exception.MemberException.MemberNotFoundException;
import com.hanghae.baedalfriend.exception.MemberException.TokenNotExistException;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Check {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;


    public Member getMemberById(String id) {
        Optional<Member> optionalMember = memberRepository.findById(Long.valueOf(id));
        return optionalMember.orElse(null);
    }


    public Member getMember() {
        return tokenProvider.getMemberFromAuthentication();
    }
}