package com.example.smart_elderly_care.domain.repo;

import com.example.smart_elderly_care.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByElderlyIdIn(List<String> elderlyIds);
    boolean existsByPhone(String phone);
    List<User> findByWelfareCenterId(Long welfareCenterId);
}
