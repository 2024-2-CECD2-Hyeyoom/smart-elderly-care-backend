package com.example.smart_elderly_care.domain.repo;

import com.example.smart_elderly_care.domain.entity.CareHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareHistoryRepository extends JpaRepository<CareHistory, Long> {
    List<CareHistory> findByUserIdIn(List<Long> userIds);
}