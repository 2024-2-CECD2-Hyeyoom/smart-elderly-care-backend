package com.example.smart_elderly_care.web.dto.care;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CareHistoryDTO {

    private Long userId;
    private LocalDate visitDate;
    private String purpose;
    private String content;
    private Long counselorId; // 상담자 id

    @Data
    @Builder
    public static class Response {
        private Long careHistoryId;
        private Long userId;
        private String userName;
        private LocalDate visitDate;
        private String purpose;
        private String content;
        private String counselorName; // 상담자 이름
    }

    @Data
    @Builder
    public static class Update {
        @NotNull
        private Long userId;         // 대상자 ID

        @NotNull
        private LocalDate visitDate; // 방문 일자

        private String purpose;      // 방문 목적
        private String content;      // 상담 내용

        @NotNull
        private Long counselorId;    // 상담자 ID
    }

    @Data
    @Builder
    public static class MyHistory {
        private Long careHistoryId;     // 돌봄 이력 ID
        private LocalDate visitDate;    // 방문 일자
        private String purpose;         // 방문 목적
        private String content;         // 상담 내용
        private String counselorName;   // 상담자 이름
    }
}
