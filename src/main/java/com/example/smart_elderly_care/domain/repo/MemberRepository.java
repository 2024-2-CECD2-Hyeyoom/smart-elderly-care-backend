package com.example.smart_elderly_care.domain.repo;

import com.example.smart_elderly_care.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByPhone(String phone);
}
