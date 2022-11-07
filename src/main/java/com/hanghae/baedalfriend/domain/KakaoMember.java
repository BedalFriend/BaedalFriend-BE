package com.hanghae.baedalfriend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.baedalfriend.shared.Authority;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.sql.Time;
import java.util.Objects;

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

    @Column(nullable = false)
    private String email;

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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Authority role;

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }
}

