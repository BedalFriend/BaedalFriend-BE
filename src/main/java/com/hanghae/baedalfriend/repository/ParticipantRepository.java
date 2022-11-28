package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.chat.entity.ParticipantNumber;
import com.hanghae.baedalfriend.shared.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {


//    ParticipantNumber  findByNumber(Long number);
}
