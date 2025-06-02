package com.example.smart_elderly_care.web.dto.sensor;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OutingEventDTO {
    private LocalDateTime outingStartTime;
    private LocalDateTime outingEndTime;
    private Long outingDurationMinutes;
}
