package com.example.smart_elderly_care.domain.repo.analysis_data;

import com.example.smart_elderly_care.domain.entity.analysis_data.SleepEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SleepEventRepository extends JpaRepository<SleepEvent, Long> {
    List<SleepEvent> findByMemberIdAndDateBetween(Long memberId, LocalDate from, LocalDate to);
}
