package com.example.smart_elderly_care.web.controller.sensor;

import com.example.smart_elderly_care.service.SensorDataService;
import com.example.smart_elderly_care.web.dto.sensor.SensorDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sensor")
@RequiredArgsConstructor
@Slf4j
public class SensorController {
    private final SensorDataService sensorDataService;

    @PostMapping
    public void receiveSensorData(@RequestBody SensorDataDTO dto) {
        log.info("Received sensors: {}, number of measurements: {}", dto.getSensor_type_name(), dto.getMeasurement_values().size());
        sensorDataService.add(dto); // 버퍼에 저장
    }
}
