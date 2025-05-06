package com.example.smart_elderly_care.domain.repo;

import com.example.smart_elderly_care.domain.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    boolean existsByPhone(String phone);
}
