package com.example.smart_elderly_care.web.dto.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class DailyHourlyDataDTO {
    private String date;
    private List<String> labels;
    private List<Integer> data;
}
