package com.hanghae.baedalfriend.dto.responsedto;

import com.hanghae.baedalfriend.shared.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto  {
    private Long id;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String email;
    private String address;
    private String profileURL;
    private Long onGoing;
    private Authority role;
}