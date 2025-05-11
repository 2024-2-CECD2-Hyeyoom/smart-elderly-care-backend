package com.example.smart_elderly_care.domain.repo.analysis_data;

import com.example.smart_elderly_care.domain.entity.analysis_data.TemperatureData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TemperatureDataRepository extends JpaRepository<TemperatureData, Long> {
    List<TemperatureData> findByMemberIdAndDateBetween(Long memberId, LocalDate from, LocalDate to);
}