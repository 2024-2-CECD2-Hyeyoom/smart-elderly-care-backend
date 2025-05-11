package com.example.smart_elderly_care.domain.repo.analysis_data;

import com.example.smart_elderly_care.domain.entity.analysis_data.ActivityData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActivityDataRepository  extends JpaRepository<ActivityData, Long> {
    List<ActivityData> findByMemberIdAndDateBetween(Long memberId, LocalDate from, LocalDate to);
}
