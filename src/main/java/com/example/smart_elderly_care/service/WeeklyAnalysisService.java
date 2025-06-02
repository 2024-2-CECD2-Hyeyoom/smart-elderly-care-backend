package com.example.smart_elderly_care.service;

import com.example.smart_elderly_care.domain.entity.analysis_data.*;
import com.example.smart_elderly_care.domain.repo.analysis_data.*;
import com.example.smart_elderly_care.web.dto.dashboard.WeeklyDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class WeeklyAnalysisService {

    private final SleepEventRepository sleepEventRepository;
    private final OutingEventRepository outingEventRepository;
    private final TemperatureDataRepository temperatureDataRepository;
    private final HumidityDataRepository humidityDataRepository;
    private final ActivityDataRepository activityDataRepository;

    public WeeklyDataDTO getWeeklyDataDTO(Long userId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toDate = LocalDate.parse(to, formatter);
        LocalDate fromDate;

        if (from == null) {
            fromDate = toDate.minusDays(6);
        } else {
            fromDate = LocalDate.parse(from, formatter);
        }

        LocalDate previousWeekStart = fromDate.minusWeeks(1);
        LocalDate previousWeekEnd = fromDate.minusDays(1);

        WeeklyDataDTO sleepDTO = getWeeklySleepDataDTO(userId, from, to);
        WeeklyDataDTO tempDTO = getWeeklyTemperatureDataDTO(userId, from, to);
        WeeklyDataDTO humidityDTO = getWeeklyHumidityDataDTO(userId, from, to);
        WeeklyDataDTO outingDTO = getWeeklyOutingDataDTO(userId, from, to);
        WeeklyDataDTO activityDTO = getWeeklyActivityDataDTO(userId, from, to);

        List<String> labels = List.of("수면", "온도", "습도", "외출 횟수", "실내 활동량");

        List<Double> currentWeekData = List.of(
                sleepDTO.getAverageCurrentWeek(),
                tempDTO.getAverageCurrentWeek(),
                humidityDTO.getAverageCurrentWeek(),
                outingDTO.getAverageCurrentWeek(),
                activityDTO.getAverageCurrentWeek()
        );

        List<Double> previousWeekData = List.of(
                sleepDTO.getAveragePreviousWeek(),
                tempDTO.getAveragePreviousWeek(),
                humidityDTO.getAveragePreviousWeek(),
                outingDTO.getAveragePreviousWeek(),
                activityDTO.getAveragePreviousWeek()
        );

        return setWeeklyDataDTO(fromDate, toDate, previousWeekStart, previousWeekEnd, labels, currentWeekData, previousWeekData, 0, 0);
    }

    public WeeklyDataDTO getWeeklySleepDataDTO(Long userId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toDate = LocalDate.parse(to, formatter);
        LocalDate fromDate;

        if (from == null) {
            fromDate = toDate.minusDays(6);
        } else {
            fromDate = LocalDate.parse(from, formatter);
        }

        LocalDate previousWeekStart = fromDate.minusWeeks(1);
        LocalDate previousWeekEnd = fromDate.minusDays(1);

        List<String> labels = IntStream.range(0, 7)
                .mapToObj(i -> fromDate.plusDays(i).toString())
                .collect(Collectors.toList());

        Map<LocalDate, Long> currentMap = sleepEventRepository
                .findByMemberIdAndDateBetween(userId, fromDate, toDate).stream()
                .collect(Collectors.groupingBy(
                        SleepEvent::getDate,
                        Collectors.summingLong(SleepEvent::getSleepDurationMinutes)
                ));

        Map<LocalDate, Long> previousMap = sleepEventRepository
                .findByMemberIdAndDateBetween(userId, previousWeekStart, previousWeekEnd).stream()
                .collect(Collectors.groupingBy(
                        SleepEvent::getDate,
                        Collectors.summingLong(SleepEvent::getSleepDurationMinutes)
                ));

        List<Double> currentWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = fromDate.plusDays(i);
                    return currentMap.getOrDefault(date, 0L) / 60.0;
                })
                .collect(Collectors.toList());

        List<Double> previousWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = previousWeekStart.plusDays(i);
                    return previousMap.getOrDefault(date, 0L) / 60.0;
                })
                .collect(Collectors.toList());

        double avgCurrent = currentWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double avgPrevious = previousWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        return setWeeklyDataDTO(fromDate, toDate, previousWeekStart, previousWeekEnd, labels, currentWeekData, previousWeekData, avgCurrent, avgPrevious);
    }

    public WeeklyDataDTO getWeeklyOutingDataDTO(Long userId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toDate = LocalDate.parse(to, formatter);
        LocalDate fromDate;

        if (from == null) {
            fromDate = toDate.minusDays(6);
        } else {
            fromDate = LocalDate.parse(from, formatter);
        }

        LocalDate previousWeekStart = fromDate.minusWeeks(1);
        LocalDate previousWeekEnd = fromDate.minusDays(1);

        List<String> labels = IntStream.range(0, 7)
                .mapToObj(i -> fromDate.plusDays(i).toString())
                .collect(Collectors.toList());

        Map<LocalDate, Long> currentMap = outingEventRepository
                .findByMemberIdAndDateBetween(userId, fromDate, toDate).stream()
                .collect(Collectors.groupingBy(
                        OutingEvent::getDate,
                        Collectors.counting()
                ));

        Map<LocalDate, Long> previousMap = outingEventRepository
                .findByMemberIdAndDateBetween(userId, previousWeekStart, previousWeekEnd).stream()
                .collect(Collectors.groupingBy(
                        OutingEvent::getDate,
                        Collectors.counting()
                ));

        List<Double> currentWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = fromDate.plusDays(i);
                    return currentMap.getOrDefault(date, 0L).doubleValue();
                })
                .collect(Collectors.toList());

        List<Double> previousWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = previousWeekStart.plusDays(i);
                    return previousMap.getOrDefault(date, 0L).doubleValue();
                })
                .collect(Collectors.toList());

        double avgCurrent = currentWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double avgPrevious = previousWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        return setWeeklyDataDTO(fromDate, toDate, previousWeekStart, previousWeekEnd, labels, currentWeekData, previousWeekData, avgCurrent, avgPrevious);
    }

    public WeeklyDataDTO getWeeklyTemperatureDataDTO(Long userId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toDate = LocalDate.parse(to, formatter);
        LocalDate fromDate;

        if (from == null) {
            fromDate = toDate.minusDays(6);
        } else {
            fromDate = LocalDate.parse(from, formatter);
        }

        LocalDate previousWeekStart = fromDate.minusWeeks(1);
        LocalDate previousWeekEnd = fromDate.minusDays(1);

        List<String> labels = IntStream.range(0, 7)
                .mapToObj(i -> fromDate.plusDays(i).toString())
                .collect(Collectors.toList());

        Map<LocalDate, List<Double>> currentMap = temperatureDataRepository
                .findByMemberIdAndDateBetween(userId, fromDate, toDate).stream()
                .collect(Collectors.groupingBy(
                        TemperatureData::getDate,
                        Collectors.mapping(TemperatureData::getTemperature, Collectors.toList())
                ));

        Map<LocalDate, List<Double>> previousMap = temperatureDataRepository
                .findByMemberIdAndDateBetween(userId, previousWeekStart, previousWeekEnd).stream()
                .collect(Collectors.groupingBy(
                        TemperatureData::getDate,
                        Collectors.mapping(TemperatureData::getTemperature, Collectors.toList())
                ));

        List<Double> currentWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = fromDate.plusDays(i);
                    return currentMap.getOrDefault(date, List.of()).stream()
                            .mapToDouble(Double::doubleValue).average().orElse(0.0);
                })
                .collect(Collectors.toList());

        List<Double> previousWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = previousWeekStart.plusDays(i);
                    return previousMap.getOrDefault(date, List.of()).stream()
                            .mapToDouble(Double::doubleValue).average().orElse(0.0);
                })
                .collect(Collectors.toList());

        double avgCurrent = currentWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double avgPrevious = previousWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        return setWeeklyDataDTO(fromDate, toDate, previousWeekStart, previousWeekEnd, labels, currentWeekData, previousWeekData, avgCurrent, avgPrevious);
    }

    public WeeklyDataDTO getWeeklyHumidityDataDTO(Long userId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toDate = LocalDate.parse(to, formatter);
        LocalDate fromDate;

        if (from == null) {
            fromDate = toDate.minusDays(6);
        } else {
            fromDate = LocalDate.parse(from, formatter);
        }

        LocalDate previousWeekStart = fromDate.minusWeeks(1);
        LocalDate previousWeekEnd = fromDate.minusDays(1);

        List<String> labels = IntStream.range(0, 7)
                .mapToObj(i -> fromDate.plusDays(i).toString())
                .collect(Collectors.toList());

        Map<LocalDate, List<Double>> currentMap = humidityDataRepository
                .findByMemberIdAndDateBetween(userId, fromDate, toDate).stream()
                .collect(Collectors.groupingBy(
                        HumidityData::getDate,
                        Collectors.mapping(HumidityData::getHumidity, Collectors.toList())
                ));

        Map<LocalDate, List<Double>> previousMap = humidityDataRepository
                .findByMemberIdAndDateBetween(userId, previousWeekStart, previousWeekEnd).stream()
                .collect(Collectors.groupingBy(
                        HumidityData::getDate,
                        Collectors.mapping(HumidityData::getHumidity, Collectors.toList())
                ));

        List<Double> currentWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = fromDate.plusDays(i);
                    return currentMap.getOrDefault(date, List.of()).stream()
                            .mapToDouble(Double::doubleValue).average().orElse(0.0);
                })
                .collect(Collectors.toList());

        List<Double> previousWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = previousWeekStart.plusDays(i);
                    return previousMap.getOrDefault(date, List.of()).stream()
                            .mapToDouble(Double::doubleValue).average().orElse(0.0);
                })
                .collect(Collectors.toList());

        double avgCurrent = currentWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double avgPrevious = previousWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        return setWeeklyDataDTO(fromDate, toDate, previousWeekStart, previousWeekEnd, labels, currentWeekData, previousWeekData, avgCurrent, avgPrevious);
    }

    public WeeklyDataDTO getWeeklyActivityDataDTO(Long userId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toDate = LocalDate.parse(to, formatter);
        LocalDate fromDate;

        if (from == null) {
            fromDate = toDate.minusDays(6);
        } else {
            fromDate = LocalDate.parse(from, formatter);
        }

        LocalDate previousWeekStart = fromDate.minusWeeks(1);
        LocalDate previousWeekEnd = fromDate.minusDays(1);

        List<String> labels = IntStream.range(0, 7)
                .mapToObj(i -> fromDate.plusDays(i).toString())
                .collect(Collectors.toList());

        Map<LocalDate, List<Double>> currentMap = activityDataRepository
                .findByMemberIdAndDateBetween(userId, fromDate, toDate).stream()
                .collect(Collectors.groupingBy(
                        ActivityData::getDate,
                        Collectors.mapping(ActivityData::getActivity, Collectors.toList())
                ));

        Map<LocalDate, List<Double>> previousMap = activityDataRepository
                .findByMemberIdAndDateBetween(userId, previousWeekStart, previousWeekEnd).stream()
                .collect(Collectors.groupingBy(
                        ActivityData::getDate,
                        Collectors.mapping(ActivityData::getActivity, Collectors.toList())
                ));

        List<Double> currentWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = fromDate.plusDays(i);
                    return currentMap.getOrDefault(date, List.of()).stream()
                            .mapToDouble(Double::doubleValue).average().orElse(0.0);
                })
                .collect(Collectors.toList());

        List<Double> previousWeekData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = previousWeekStart.plusDays(i);
                    return previousMap.getOrDefault(date, List.of()).stream()
                            .mapToDouble(Double::doubleValue).average().orElse(0.0);
                })
                .collect(Collectors.toList());

        double avgCurrent = currentWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double avgPrevious = previousWeekData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        return setWeeklyDataDTO(fromDate, toDate, previousWeekStart, previousWeekEnd, labels, currentWeekData, previousWeekData, avgCurrent, avgPrevious);
    }

    private WeeklyDataDTO setWeeklyDataDTO(LocalDate fromDate, LocalDate toDate, LocalDate previousWeekStart, LocalDate previousWeekEnd,
                                           List<String> labels, List<Double> currentWeekData, List<Double> previousWeekData,
                                           double avgCurrent, double avgPrevious) {

        WeeklyDataDTO dto = new WeeklyDataDTO();
        WeeklyDataDTO.Period period = new WeeklyDataDTO.Period();
        WeeklyDataDTO.Period.DateRange currentRange = new WeeklyDataDTO.Period.DateRange();

        currentRange.setFrom(fromDate.toString());
        currentRange.setTo(toDate.toString());

        WeeklyDataDTO.Period.DateRange previousRange = new WeeklyDataDTO.Period.DateRange();
        previousRange.setFrom(previousWeekStart.toString());
        previousRange.setTo(previousWeekEnd.toString());

        period.setCurrentWeek(currentRange);
        period.setPreviousWeek(previousRange);

        WeeklyDataDTO.DataSection data = new WeeklyDataDTO.DataSection();
        data.setCurrentWeek(currentWeekData);
        data.setPreviousWeek(previousWeekData);

        dto.setPeriod(period);
        dto.setLabels(labels);
        dto.setData(data);
        dto.setAverageCurrentWeek(avgCurrent);
        dto.setAveragePreviousWeek(avgPrevious);

        return dto;
    }
}
