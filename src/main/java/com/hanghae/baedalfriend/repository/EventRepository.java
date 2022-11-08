package com.hanghae.baedalfriend.repository;

import com.hanghae.baedalfriend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
