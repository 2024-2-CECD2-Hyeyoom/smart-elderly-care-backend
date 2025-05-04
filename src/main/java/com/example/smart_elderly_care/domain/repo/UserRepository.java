package com.example.smart_elderly_care.domain.repo;

import com.example.smart_elderly_care.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhone(String phone);
}
