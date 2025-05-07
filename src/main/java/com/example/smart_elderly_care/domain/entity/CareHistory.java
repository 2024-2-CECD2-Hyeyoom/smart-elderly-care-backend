package com.example.smart_elderly_care.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "care_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;  // 대상자 id

    private LocalDate visitDate;  // 방문 일자

    private String purpose;  // 방문 목적

    private String content;  // 상담 내용

    private Long counselorId;  // 상담자 id
}
