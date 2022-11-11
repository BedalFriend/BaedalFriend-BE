package com.hanghae.baedalfriend.dto.requestdto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.servlet.http.HttpServletResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    // 토큰에 담기는 값
    public void tokenToHeaders(HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + getAccessToken()); //엑세스 토큰
        response.addHeader("Refresh_Token", getRefreshToken()); //리프레시 토큰
        response.addHeader("Access-Token-Expire-Time", getAccessTokenExpiresIn().toString()); //엑세스토큰 만료시간
    }
}