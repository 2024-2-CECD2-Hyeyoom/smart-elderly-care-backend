package com.example.smart_elderly_care.web.dto.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class WeeklyDataDTO {
    private Period period;
    private List<String> labels;
    private DataSection data;
    private double averageCurrentWeek;
    private double averagePreviousWeek;

    @Data
    public static class Period {
        private DateRange currentWeek;
        private DateRange previousWeek;

        @Data
        public static class DateRange {
            private String from;
            private String to;
        }
    }

    @Data
    public static class DataSection {
        private List<Double> currentWeek;
        private List<Double> previousWeek;
    }

}
