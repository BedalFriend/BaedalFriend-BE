package com.hanghae.baedalfriend.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.baedalfriend.shared.Authority;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Entity
public class KakaoMember extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String profileURL;

    @Column(unique = true)
    private Long kakaoId;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Enumerated(value = EnumType.STRING)
    private Authority role;


}