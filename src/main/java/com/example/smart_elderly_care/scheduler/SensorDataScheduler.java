package com.example.smart_elderly_care.scheduler;

import com.example.smart_elderly_care.domain.entity.analysis_data.OutingEvent;
import com.example.smart_elderly_care.domain.entity.analysis_data.SleepEvent;
import com.example.smart_elderly_care.service.SensorDataService;
import com.example.smart_elderly_care.web.dto.sensor.OutingEventDTO;
import com.example.smart_elderly_care.web.dto.sensor.SensorAnalysisResultDTO;
import com.example.smart_elderly_care.web.dto.sensor.SensorDataDTO;
import com.example.smart_elderly_care.web.dto.sensor.SleepEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.smart_elderly_care.domain.repo.analysis_data.OutingEventRepository;
import com.example.smart_elderly_care.domain.repo.analysis_data.SleepEventRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorDataScheduler {
    private final SensorDataService sensorDataService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final SleepEventRepository sleepEventRepository;
    private final OutingEventRepository outingEventRepository;

    @Scheduled(fixedRate = 120000)
    public void sendDataToPython() {
        List<SensorDataDTO> batch = sensorDataService.flush();

        if (batch.isEmpty()) {
            log.info("보낼 데이터 없음");
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<SensorDataDTO>> request = new HttpEntity<>(batch, headers);

            ResponseEntity<SensorAnalysisResultDTO> response = restTemplate.exchange(
                    "http://localhost:8000/analyze-sensor",
                    HttpMethod.POST,
                    request,
                    SensorAnalysisResultDTO.class
            );

            SensorAnalysisResultDTO result = response.getBody();

            if (result != null) {
                for (SleepEventDTO s : result.getSleepEvents()) {
                    SleepEvent event = SleepEvent.builder()
                            .sleepStartTime(s.getSleepStartTime())
                            .sleepEndTime(s.getSleepEndTime())
                            .build(); // Duration은 자동 계산
                    sleepEventRepository.save(event);
                }

                for (OutingEventDTO o : result.getOutingEvents()) {
                    OutingEvent event = OutingEvent.builder()
                            .outingStartTime(o.getOutingStartTime())
                            .outingEndTime(o.getOutingEndTime())
                            .build(); // Duration 자동 계산
                    outingEventRepository.save(event);
                }

                log.info("✅ 분석 결과 저장 완료: 수면 {}건, 외출 {}건", result.getSleepEvents().size(), result.getOutingEvents().size());
            }

        } catch (Exception e) {
            log.error("❌ Python 서버 전송 실패: {}", e.getMessage(), e);
        }
    }
}
