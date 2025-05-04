package com.example.smart_elderly_care.domain.repo;

import com.example.smart_elderly_care.domain.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByPhone(String phone);
}
