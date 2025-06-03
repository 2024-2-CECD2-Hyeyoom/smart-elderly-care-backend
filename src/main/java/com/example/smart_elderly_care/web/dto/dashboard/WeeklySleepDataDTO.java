package com.example.smart_elderly_care.web.dto.dashboard;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeeklySleepDataDTO extends WeeklyDataDTO{
    private LocalDateTime sleepStartTime;
    private LocalDateTime sleepEndTime;
}
