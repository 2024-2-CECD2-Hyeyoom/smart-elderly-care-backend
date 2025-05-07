package com.example.smart_elderly_care.web.controller.care;

import com.example.smart_elderly_care.apiPayload.ApiResponse;
import com.example.smart_elderly_care.apiPayload.code.SuccessStatus;
import com.example.smart_elderly_care.service.CareService;
import com.example.smart_elderly_care.web.dto.care.CareHistoryDTO;
import com.example.smart_elderly_care.web.dto.care.CareHistoryResponseDTO;
import com.example.smart_elderly_care.web.dto.care.CareTargetResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/care")
@RequiredArgsConstructor
public class CareController {

    private final CareService careService;

    @Operation(summary = "돌봄 대상자 조회 API", description = "보호자/담당자의 돌봄 대상자를 조회합니다.")
    @GetMapping("/{userId}/targets")
    public ApiResponse<List<CareTargetResponseDTO>> getCareTargets(@PathVariable Long userId) {
        List<CareTargetResponseDTO> careTargets = careService.getCareTargetsByManager(userId);
        return ApiResponse.of(SuccessStatus.CARE_TARGETS_OK, careTargets);
    }

    @Operation(summary = "돌봄 이력 추가 API", description = "대상자 돌봄 이력을 추가합니다.")
    @PostMapping("/history")
    public ApiResponse<String> addCareHistory(@Valid @RequestBody CareHistoryDTO dto) {
        careService.addCareHistory(dto);
        return ApiResponse.of(SuccessStatus.CARE_HISTORY_ADD_OK, "돌봄 이력 추가 성공");
    }

    @Operation(summary = "돌봄 대상자 돌봄 이력 조회 API", description = "보호자/담당자의 돌봄 대상자의 돌봄 이력을 조회합니다.")
    @GetMapping("/{userId}/targets/history")
    public ApiResponse<List<CareHistoryResponseDTO>> getCareHistoriesByManager(@PathVariable Long userId) {
        List<CareHistoryResponseDTO> histories = careService.getCareHistoriesByManager(userId);
        return ApiResponse.of(SuccessStatus.CARE_HISTORY_OK, histories);
    }
}
