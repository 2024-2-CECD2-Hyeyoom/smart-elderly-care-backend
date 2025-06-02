package com.example.smart_elderly_care.web.dto.sensor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SensorDataDTO {
    private String sensor_type_name;
    private String measurement_time;
    private List<Float> measurement_values;
}

