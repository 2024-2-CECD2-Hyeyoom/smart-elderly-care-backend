package com.example.smart_elderly_care.web.dto.sensor;

import lombok.Data;

import java.util.List;

@Data
public class SensorAnalysisResultDTO {
    private List<SleepEventDTO> sleepEvents;
    private List<OutingEventDTO> outingEvents;
}
