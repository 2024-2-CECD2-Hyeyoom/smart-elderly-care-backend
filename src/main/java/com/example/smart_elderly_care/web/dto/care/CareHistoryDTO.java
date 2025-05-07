package com.example.smart_elderly_care.web.dto.care;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CareHistoryDTO {
    private Long userId;
    private LocalDate visitDate;
    private String purpose;
    private String content;
    private Long counselorId; // 상담자 id
}
