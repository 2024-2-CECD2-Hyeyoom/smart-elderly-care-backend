package com.example.smart_elderly_care.domain.repo.analysis_data;

import com.example.smart_elderly_care.domain.entity.analysis_data.OutingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutingEventRepository extends JpaRepository<OutingEvent, Long> {
    List<OutingEvent> findByMemberIdAndDateBetween(Long memberId, LocalDate from, LocalDate to);
    List<OutingEvent> findByMemberIdAndOutingStartTimeBetween(Long memberId, LocalDateTime start, LocalDateTime end);
}