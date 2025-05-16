package com.example.smart_elderly_care.service;

import com.example.smart_elderly_care.domain.entity.analysis_data.HumidityData;
import com.example.smart_elderly_care.domain.entity.analysis_data.OutingEvent;
import com.example.smart_elderly_care.domain.entity.analysis_data.SleepEvent;
import com.example.smart_elderly_care.domain.entity.analysis_data.TemperatureData;
import com.example.smart_elderly_care.domain.repo.analysis_data.*;
import com.example.smart_elderly_care.web.dto.dashboard.DailyEventDTO;
import com.example.smart_elderly_care.web.dto.dashboard.DailyHourlyDataDTO;
import com.example.smart_elderly_care.web.dto.dashboard.WeeklyDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DailyAnalysisService {
    private final SleepEventRepository sleepEventRepository;
    private final OutingEventRepository outingEventRepository;
    private final TemperatureDataRepository temperatureDataRepository;
    private final HumidityDataRepository humidityDataRepository;

    public DailyEventDTO getDailySleepDataDTO(Long userId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();

        List<SleepEvent> sleepEvents = sleepEventRepository.findByMemberIdAndSleepStartTimeBetween(userId, startOfDay, endOfDay);

        List<DailyEventDTO.Event> eventList = new ArrayList<>();
        for (SleepEvent sleepEvent : sleepEvents) {
            DailyEventDTO.Event start = new DailyEventDTO.Event();
            start.setTimestamp(sleepEvent.getSleepStartTime().toString());
            start.setStatus(1);

            DailyEventDTO.Event end = new DailyEventDTO.Event();
            end.setTimestamp(sleepEvent.getSleepEndTime().toString());
            end.setStatus(0);

            eventList.add(start);
            eventList.add(end);
        }

        eventList.sort(Comparator.comparing(DailyEventDTO.Event::getTimestamp));

        DailyEventDTO dto = new DailyEventDTO();
        dto.setDate(date);
        dto.setEvents(eventList);

        return dto;
    }

    public DailyEventDTO getDailyOutingDataDTO(Long userId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();

        List<OutingEvent> outingEvents = outingEventRepository.findByMemberIdAndOutingStartTimeBetween(userId, startOfDay, endOfDay);

        List<DailyEventDTO.Event> eventList = new ArrayList<>();
        for (OutingEvent outingEvent : outingEvents) {
            DailyEventDTO.Event start = new DailyEventDTO.Event();
            start.setTimestamp(outingEvent.getOutingStartTime().toString());
            start.setStatus(1);

            DailyEventDTO.Event end = new DailyEventDTO.Event();
            end.setTimestamp(outingEvent.getOutingEndTime().toString());
            end.setStatus(0);

            eventList.add(start);
            eventList.add(end);
        }

        eventList.sort(Comparator.comparing(DailyEventDTO.Event::getTimestamp));

        DailyEventDTO dto = new DailyEventDTO();
        dto.setDate(date);
        dto.setEvents(eventList);

        return dto;
    }

    public DailyHourlyDataDTO getDailyHumidityDataDTO(Long userId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<HumidityData> dataList = humidityDataRepository
                .findByMemberIdAndRecordedTimeBetween(userId, start, end);

        List<String> labels = IntStream.range(0, 24)
                .mapToObj(i -> String.format("%02d:00", i))
                .collect(Collectors.toList());

        List<Integer> data = IntStream.range(0, 24)
                .mapToObj(hour -> {
                    List<Double> values = dataList.stream()
                            .filter(d -> d.getRecordedTime().getHour() == hour)
                            .map(HumidityData::getHumidity)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    return values.isEmpty() ? 0 : (int) Math.round(values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
                })
                .collect(Collectors.toList());

        DailyHourlyDataDTO dto = new DailyHourlyDataDTO();
        dto.setDate(dateStr);
        dto.setLabels(labels);
        dto.setData(data);

        return dto;
    }

    public DailyHourlyDataDTO getDailyTemperatureDataDTO(Long userId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<TemperatureData> dataList = temperatureDataRepository
                .findByMemberIdAndRecordedTimeBetween(userId, start, end);

        List<String> labels = IntStream.range(0, 24)
                .mapToObj(i -> String.format("%02d:00", i))
                .collect(Collectors.toList());

        List<Integer> data = IntStream.range(0, 24)
                .mapToObj(hour -> {
                    List<Double> values = dataList.stream()
                            .filter(d -> d.getRecordedTime().getHour() == hour)
                            .map(TemperatureData::getTemperature)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    return values.isEmpty() ? 0 : (int) Math.round(values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
                })
                .collect(Collectors.toList());

        DailyHourlyDataDTO dto = new DailyHourlyDataDTO();
        dto.setDate(dateStr);
        dto.setLabels(labels);
        dto.setData(data);

        return dto;
    }
}
