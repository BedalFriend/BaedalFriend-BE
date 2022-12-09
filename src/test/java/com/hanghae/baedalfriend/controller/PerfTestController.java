package com.hanghae.baedalfriend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
class PerfTestController {

   @GetMapping("posts/{postsId}")
   public String getPosts(@PathVariable Long postsId) {
       return "perfTest postsId : " + postsId;
   }
}