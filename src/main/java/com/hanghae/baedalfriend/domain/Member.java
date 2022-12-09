package com.hanghae.baedalfriend.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.baedalfriend.shared.Authority;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private Long kakaoId;
    private String address;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    private String profileURL;
    @Enumerated(value = EnumType.STRING)
    private Authority role;

    public Member(String encodedPassword, String profileURL, String nickname, Long kakaoId, String email) {
        this.nickname = nickname;
        this.password = encodedPassword;
        this.profileURL = profileURL;
        this.kakaoId = kakaoId;
        this.email = email;
        this.role = Authority.ROLE_MEMBER;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || Hibernate.getClass(this) != Hibernate.getClass(object)) {
            return false;
        }
        Member member = (Member) object;
        return id != null & Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public void update(String nickname, String profileURL) {
        this.nickname = nickname;
        this.profileURL = profileURL;
    }
}