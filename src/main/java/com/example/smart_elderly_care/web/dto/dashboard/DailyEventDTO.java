package com.example.smart_elderly_care.web.dto.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class DailyEventDTO {
    private String date;
    private List<Event> events;

    @Data
    public static class Event {
        private String timestamp;
        private int status;
    }
}
