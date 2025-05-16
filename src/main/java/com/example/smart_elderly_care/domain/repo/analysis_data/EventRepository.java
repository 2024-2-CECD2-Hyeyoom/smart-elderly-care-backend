package com.example.smart_elderly_care.domain.repo.analysis_data;

import com.example.smart_elderly_care.domain.entity.analysis_data.EventData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventData, Long> {
}
