package com.example.smart_elderly_care.web.dto.sensor;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SleepEventDTO {
    private LocalDateTime sleepStartTime;
    private LocalDateTime sleepEndTime;
    private Long sleepDurationMinutes;
}
