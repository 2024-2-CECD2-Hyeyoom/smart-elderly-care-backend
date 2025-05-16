package com.example.smart_elderly_care.web.controller.dashboard;

import com.example.smart_elderly_care.apiPayload.ApiResponse;
import com.example.smart_elderly_care.apiPayload.code.SuccessStatus;
import com.example.smart_elderly_care.service.WeeklyAnalysisService;
import com.example.smart_elderly_care.web.dto.dashboard.WeeklyDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis/{userID}")
@RequiredArgsConstructor
public class AnalysisController {
    private final WeeklyAnalysisService weeklyAnalysisService;

    @Operation(summary = "주간 데이터 조회 API", description = "주간 데이터 조회 결과입니다.")
    @GetMapping("/weekly")
    public ApiResponse<WeeklyDataDTO> getWeeklyAnalysis(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        WeeklyDataDTO dto = weeklyAnalysisService.getWeeklyDataDTO(userId, from, to);
        return ApiResponse.of(SuccessStatus.WEEKLY_REPORT_OK, dto);
    }

    @Operation(summary = "수면 데이터 조회 API", description = "수면 데이터 조회 결과입니다.")
    @GetMapping("/sleep")
    public ApiResponse<WeeklyDataDTO> getSleepAnalysis(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        WeeklyDataDTO dto = weeklyAnalysisService.getWeeklySleepDataDTO(userId, from, to);
        return ApiResponse.of(SuccessStatus.SLEEP_ANALYSIS_REPORT_OK, dto);
    }

    @Operation(summary = "외출 데이터 조회 API", description = "외출 데이터 조회 결과입니다.")
    @GetMapping("/outing")
    public ApiResponse<WeeklyDataDTO> getOutingAnalysis(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        WeeklyDataDTO dto = weeklyAnalysisService.getWeeklyOutingDataDTO(userId, from, to);
        return ApiResponse.of(SuccessStatus.OUTING_ANALYSIS_REPORT_OK, dto);
    }

    @Operation(summary = "온도 데이터 조회 API", description = "온도 데이터 조회 결과입니다.")
    @GetMapping("/temperature")
    public ApiResponse<WeeklyDataDTO> getTemperatureAnalysis(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        WeeklyDataDTO dto = weeklyAnalysisService.getWeeklyTemperatureDataDTO(userId, from, to);
        return ApiResponse.of(SuccessStatus.TEMPERATURE_ANALYSIS_REPORT_OK, dto);
    }

    @Operation(summary = "습도 데이터 조회 API", description = "습도 데이터 조회 결과입니다.")
    @GetMapping("/humidity")
    public ApiResponse<WeeklyDataDTO> getHumidityAnalysis(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        WeeklyDataDTO dto = weeklyAnalysisService.getWeeklyHumidityDataDTO(userId, from, to);
        return ApiResponse.of(SuccessStatus.HUMIDITY_ANALYSIS_REPORT_OK, dto);
    }
}
