package com.hanghae.baedalfriend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableJpaAuditing
@SpringBootApplication
public class BaedalFriendBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaedalFriendBeApplication.class, args);

    }
}
