package com.example.smart_elderly_care.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "welfare_center")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WelfareCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sido;           // 시도
    private String sigungu;        // 시군구

    @Column(unique = true, nullable = false)
    private String organName;      // 운영기관명

    private String address;        // 상세주소
    private String phone;          // 연락처
    private String bizType;        // 업무구분
    private String organType;      // 기관유형
}


