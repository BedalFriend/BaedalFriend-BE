package com.hanghae.baedalfriend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    @DisplayName("정상 케이스")
    void getRoomTitle() {
        Post post = Post.builder()
                .id(1L)
                .member(Member.builder().id(1L).build())
                .roomTitle("baedalfriend")
                .isDone(false)
                .content("content")
                .category("category")
                .targetName("targetName")
                .targetAddress("targetAddress")
                .targetAmount(10000L)
                .deliveryTime("deliveryTime")
                .deliveryFee(1000L)
                .gatherName("gatherName")
                .gatherAddress("gatherAddress")
                .maxCapacity(10L)
                .region("region")
                .keyword("keyword")
                .nickname( "nickname")
                .profileURL("profileURL")
                .hits(0L)
                .participantNumber(0L)
                .isClosed(false)
                .build();
        assertNotNull(post);
        assertEquals(post.getRoomTitle(), "baedalfriend");

    }

    @Test
    void update() {


    }

    @Test
    void validateMember() {
    }

    @Test
    void isDone() {
    }

    @Test
    void hitsPost() {
    }

    @Test
    void updateParticipantNumber() {
    }

    @Test
    void decreaseParticipantNumber() {
    }

    @Test
    void isClosed() {
    }

    @Test
    void getId() {
    }

    @Test
    void getMember() {
    }

    @Test
    void testGetRoomTitle() {
    }

    @Test
    void testIsDone() {
    }

    @Test
    void getContent() {
    }

    @Test
    void getCategory() {
    }

    @Test
    void getTargetName() {
    }

    @Test
    void getTargetAddress() {
    }

    @Test
    void getTargetAmount() {
    }

    @Test
    void getDeliveryTime() {
    }

    @Test
    void getDeliveryFee() {
    }

    @Test
    void getGatherName() {
    }

    @Test
    void getGatherAddress() {
    }

    @Test
    void getLimitTime() {
    }

    @Test
    void getMaxCapacity() {
    }

    @Test
    void getRegion() {
    }

    @Test
    void getKeyword() {
    }

    @Test
    void getNickname() {
    }

    @Test
    void getProfileURL() {
    }

    @Test
    void getHits() {
    }

    @Test
    void getParticipantNumber() {
    }

    @Test
    void testIsClosed() {
    }

    @Test
    void setId() {
    }

    @Test
    void setMember() {
    }

    @Test
    void setRoomTitle() {
    }

    @Test
    void setDone() {
    }

    @Test
    void setContent() {
    }

    @Test
    void setCategory() {
    }

    @Test
    void setTargetName() {
    }

    @Test
    void setTargetAddress() {
    }

    @Test
    void setTargetAmount() {
    }

    @Test
    void setDeliveryTime() {
    }

    @Test
    void setDeliveryFee() {
    }

    @Test
    void setGatherName() {
    }

    @Test
    void setGatherAddress() {
    }

    @Test
    void setLimitTime() {
    }

    @Test
    void setMaxCapacity() {
    }

    @Test
    void setRegion() {
    }

    @Test
    void setKeyword() {
    }

    @Test
    void setNickname() {
    }

    @Test
    void setProfileURL() {
    }

    @Test
    void setHits() {
    }

    @Test
    void setParticipantNumber() {
    }

    @Test
    void setClosed() {
    }

    @Test
    void builder() {
    }
}