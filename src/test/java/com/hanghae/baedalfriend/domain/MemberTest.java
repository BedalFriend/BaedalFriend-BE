package com.hanghae.baedalfriend.domain;

import com.hanghae.baedalfriend.shared.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    @Test
    @DisplayName("정상 케이스")
    void createMember() {
        Member member = Member.builder()
                .nickname("test")
                .email("test@gmail.com")
                .kakaoId(123456789L)
                .address("서울시 강남구")
                .password("1q2w3e4r!!")
                .profileURL("https://test.com")
                .build();
        assertNotNull(member);
    }

    @Test
    void testEquals() {
        Member member = Member.builder()
                .nickname("test")
                .email("test@gmail.com")
                .kakaoId(123456789L)
                .address("서울시 강남구")
                .password("1q2w3e4r!!")
                .profileURL("https://test.com")
                .build();
    }

    @Test
    void testHashCode() {
        Member member = Member.builder()
                .nickname("test")
                .email("test@gmail.com")
                .kakaoId(123456789L)
                .address("서울시 강남구")
                .password("1q2w3e4r!!")
                .profileURL("https://test.com")
                .build();
    }

    @Test
    void validatePassword() {
        Member member = Member.builder()
                .nickname("test")
                .email("test@gmail.com")
                .kakaoId(123456789L)
                .address("서울시 강남구")
                .password("1q2w3e4r!!")
                .profileURL("https://test.com")
                .build();

        if (member.getPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }

        if (member.getPassword().length() > 20) {
            throw new IllegalArgumentException("비밀번호는 20자 이하여야 합니다.");
        }

        if (!member.getPassword().matches(".*[0-9].*")) {
            throw new IllegalArgumentException("비밀번호는 숫자를 포함해야 합니다.");
        }

        if (!member.getPassword().matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("비밀번호는 영문자를 포함해야 합니다.");
        }

        if (!member.getPassword().matches(".*[!@#$%^&*()_+|<>?:{}].*")) {
            throw new IllegalArgumentException("비밀번호는 특수문자를 포함해야 합니다.");
        }
    }

    @Test
    void getId() {
        Member member = Member.builder()
                .id(1L)
                .nickname("test")
                .email("test@gmail.com")
                .kakaoId(123456789L)
                .build();
        assertEquals(1L, member.getId());
    }

    @Test
    void getNickname() {
        Member member = Member.builder()
                .nickname("test")
                .build();
        assertEquals("test", member.getNickname());
    }

    @Test
    void getEmail() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();
        assertEquals("test@gmail.com", member.getEmail());
    }

    @Test
    void getKakaoId() {
        Member member = Member.builder()
                .kakaoId(123456789L)
                .build();
        assertEquals(123456789L, member.getKakaoId());
    }

    @Test
    void getAddress() {
        Member member = Member.builder()
                .address("서울시 강남구")
                .build();
        assertEquals("서울시 강남구", member.getAddress());
    }

    @Test
    void getPassword() {
        Member member = Member.builder()
                .password("1q2w3e4r!!")
                .build();
        assertEquals("1q2w3e4r!!", member.getPassword());
    }

    @Test
    void getProfileURL() {
        Member member = Member.builder()
                .profileURL("https://test.com")
                .build();
        assertEquals("https://test.com", member.getProfileURL());
    }

    @Test
    void getRole() {
        Member member = Member.builder()
                .role(Authority.ROLE_MEMBER)
                .build();
        assertEquals(Authority.ROLE_MEMBER, member.getRole());
    }

    @Test
    void setId() {
        Member member = Member.builder()
                .id(2L)
                .build();
        member.setId(2L);
        assertEquals(2L, member.getId());
    }

    @Test
    void setNickname() {
        Member member = Member.builder()
                .nickname("test")
                .build();
        member.setNickname("test");
        assertEquals("test", member.getNickname());
    }

    @Test
    void setEmail() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();
        member.setEmail("test@gmail.com");
        assertEquals("test@gmail.com", member.getEmail());
    }

    @Test
    void setKakaoId() {
        Member member = Member.builder()
                .kakaoId(123456789L)
                .build();
        member.setKakaoId(123456789L);
        assertEquals(123456789L, member.getKakaoId());
    }

    @Test
    void setAddress() {
        Member member = Member.builder()
                .address("서울시 강남구")
                .build();
        member.setAddress("서울시 강남구");
        assertEquals("서울시 강남구", member.getAddress());
    }

    @Test
    void setPassword() {
        Member member = Member.builder()
                .password("1q2w3e4r!!")
                .build();
        member.setPassword("1q2w3e4r!!");
        assertEquals("1q2w3e4r!!", member.getPassword());
    }

    @Test
    void setProfileURL() {
        Member member = Member.builder()
                .profileURL("https://test.com")
                .build();
        member.setProfileURL("https://test.com");
        assertEquals("https://test.com", member.getProfileURL());
    }

    @Test
    void setRole() {
        Member member = Member.builder()
                .role(Authority.ROLE_MEMBER)
                .build();
        member.setRole(Authority.ROLE_MEMBER);
        assertEquals(Authority.ROLE_MEMBER, member.getRole());
    }

    @Test
    void builder() {
        Member member = Member.builder()
                .id(1L)
                .nickname("test")
                .email("test@gmail.com")

                .password("1q2w3e4r!!")
                .kakaoId(123456789L)
                .profileURL("https://test.com")
                .role(Authority.ROLE_MEMBER)
                .build();
        assertEquals(1L, member.getId());
        assertEquals("test", member.getNickname());
        assertEquals("test@gmail.com", member.getEmail());
        assertEquals(123456789L, member.getKakaoId());
        assertEquals("서울시 강남구", member.getAddress());
        assertEquals("1q2w3e4r!!", member.getPassword());
        assertEquals("https://test.com", member.getProfileURL());
        assertEquals(Authority.ROLE_MEMBER, member.getRole());

    }
}