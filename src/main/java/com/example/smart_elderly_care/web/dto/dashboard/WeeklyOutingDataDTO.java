package com.example.smart_elderly_care.web.dto.dashboard;

import lombok.Data;

@Data
public class WeeklyOutingDataDTO extends WeeklyDataDTO{
    private Long outingDurationMinutes;
}
