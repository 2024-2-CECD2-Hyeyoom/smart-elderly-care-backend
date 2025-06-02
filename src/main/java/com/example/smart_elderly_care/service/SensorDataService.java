package com.example.smart_elderly_care.service;
import com.example.smart_elderly_care.web.dto.sensor.SensorDataDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SensorDataService {
    private final List<SensorDataDTO> buffer = new CopyOnWriteArrayList<>();

    public void add(SensorDataDTO dto) {
        buffer.add(dto);
    }

    public List<SensorDataDTO> flush() {
        List<SensorDataDTO> snapshot = new ArrayList<>(buffer);
        buffer.clear();
        return snapshot;
    }
}
