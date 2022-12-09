package com.hanghae.baedalfriend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.*;

@RestController
class MemberControllerTest {

   @GetMapping("posts/{postsId}")
   public String getPosts(@PathVariable Long postsId) {
       return "memberTest postsId : " + postsId;
   }


    @Test
    void email() {
    }

    @Test
    void nickname() {
    }

    @Test
    void signup() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void reissue() {
    }
}