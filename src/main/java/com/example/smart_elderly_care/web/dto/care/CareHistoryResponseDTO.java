package com.example.smart_elderly_care.web.dto.care;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CareHistoryResponseDTO {
    private Long careHistoryId;
    private Long userId;
    private String userName;
    private LocalDate visitDate;
    private String purpose;
    private String content;
    private String counselorName;  // 상담자 이름
}
