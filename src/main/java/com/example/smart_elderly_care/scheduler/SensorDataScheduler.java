package com.example.smart_elderly_care.scheduler;

import com.example.smart_elderly_care.domain.entity.analysis_data.OutingEvent;
import com.example.smart_elderly_care.domain.entity.analysis_data.SleepEvent;
import com.example.smart_elderly_care.domain.entity.member.Member;
import com.example.smart_elderly_care.domain.entity.member.User;
import com.example.smart_elderly_care.domain.repo.UserRepository;
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
    private final UserRepository userRepository;

    @Scheduled(fixedRate = 120000)
    public void sendDataToPython() {
        List<SensorDataDTO> batch = sensorDataService.flush();

        if (batch.isEmpty()) {
            log.info("No data to send.");
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
            User tempUser = userRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("id=1인 유저가 존재하지 않습니다."));

            if (result != null) {
                for (SleepEventDTO s : result.getSleepEvents()) {
                    SleepEvent event = SleepEvent.builder()
                            .sleepStartTime(s.getSleepStartTime())
                            .sleepEndTime(s.getSleepEndTime())
                            .member(tempUser)
                            .date(s.getSleepStartTime().toLocalDate())
                            .build(); // Duration은 자동 계산
                    sleepEventRepository.save(event);
                }

                for (OutingEventDTO o : result.getOutingEvents()) {
                    OutingEvent event = OutingEvent.builder()
                            .outingStartTime(o.getOutingStartTime())
                            .outingEndTime(o.getOutingEndTime())
                            .member(tempUser)
                            .date(o.getOutingStartTime().toLocalDate())
                            .build(); // Duration 자동 계산
                    outingEventRepository.save(event);
                }

                log.info("✅ Analysis results saved: {} sleep events, {} outing events.",
                        result.getSleepEvents().size(), result.getOutingEvents().size());
            }

        } catch (Exception e) {
            log.error("❌ Failed to send data to Python server: {}", e.getMessage(), e);
        }
    }
}
