package com.example.smart_elderly_care.domain.repo;

import com.example.smart_elderly_care.domain.entity.WelfareCenter;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WelfareCenterRepository extends JpaRepository<WelfareCenter, Long> {
    boolean existsByOrganName(String organName); // 중복 저장 방지

    List<WelfareCenter> findBySido(String sido);
    List<WelfareCenter> findBySidoAndSigungu(String sido, String sigungu);

    Optional<WelfareCenter> findByOrganName(String welfareCenterName);
}
