package com.example.smart_elderly_care.web.controller.care;

import com.example.smart_elderly_care.apiPayload.ApiResponse;
import com.example.smart_elderly_care.apiPayload.code.SuccessStatus;
import com.example.smart_elderly_care.service.CareService;
import com.example.smart_elderly_care.web.dto.care.*;
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
    public ApiResponse<List<CareTargetDTO>> getCareTargets(@PathVariable Long userId) {
        List<CareTargetDTO> careTargets = careService.getCareTargetsByManager(userId);
        return ApiResponse.of(SuccessStatus.CARE_TARGETS_OK, careTargets);
    }

    @Operation(summary = "돌봄 대상자 이름 목록 조회 API", description = "돌봄 이력 추가 시, 보호자/담당자의 관리 중인 대상자의 id 및 이름을 불러옵니다.")
    @GetMapping("/{userId}/targets/names")
    public ApiResponse<List<CareTargetDTO.Name>> getCareTargetNames(@PathVariable Long userId) {
        List<CareTargetDTO.Name> careTargetNames = careService.getCareTargetNamesByManager(userId);
        return ApiResponse.of(SuccessStatus.CARE_TARGET_NAMES_OK, careTargetNames);
    }

    @Operation(summary = "돌봄 대상자 프로필 카드 조회 API", description = "노인의 기본 프로필 정보를 조회합니다.")
    @GetMapping("/{userId}/profile")
    public ApiResponse<CareTargetDTO.Profile> getCareTargetProfile(@PathVariable Long userId) {
        CareTargetDTO.Profile profile = careService.getCareTargetProfile(userId);
        return ApiResponse.of(SuccessStatus.CARE_PROFILE_OK, profile);
    }

    @Operation(summary = "돌봄 이력 추가 API", description = "대상자 돌봄 이력을 추가합니다.")
    @PostMapping("/history")
    public ApiResponse<String> addCareHistory(@Valid @RequestBody CareHistoryDTO dto) {
        careService.addCareHistory(dto);
        return ApiResponse.of(SuccessStatus.CARE_HISTORY_ADD_OK, "돌봄 이력 추가 성공");
    }

    @Operation(summary = "돌봄 대상자 돌봄 이력 조회 API", description = "보호자/담당자의 돌봄 대상자의 돌봄 이력을 조회합니다.")
    @GetMapping("/{userId}/targets/history")
    public ApiResponse<List<CareHistoryDTO.Response>> getCareHistoriesByManager(@PathVariable Long userId) {
        List<CareHistoryDTO.Response> histories = careService.getCareHistoriesByManager(userId);
        return ApiResponse.of(SuccessStatus.CARE_HISTORY_OK, histories);
    }

    @Operation(summary = "본인 돌봄 이력 조회 API", description = "노인이 자신의 돌봄 이력을 조회합니다.")
    @GetMapping("/{userId}/history")
    public ApiResponse<List<CareHistoryDTO.MyHistory>> getMyCareHistories(@PathVariable Long userId) {
        List<CareHistoryDTO.MyHistory> histories = careService.getCareHistoriesForUser(userId);
        return ApiResponse.of(SuccessStatus.CARE_HISTORY_OK, histories);
    }

    @Operation(summary = "돌봄 이력 수정 API", description = "대상자의 돌봄 이력을 수정합니다.")
    @PutMapping("/history/{careHistoryId}")
    public ApiResponse<String> updateCareHistory(
            @PathVariable Long careHistoryId,
            @Valid @RequestBody CareHistoryDTO.Update dto
    ) {
        careService.updateCareHistory(careHistoryId, dto);
        return ApiResponse.of(SuccessStatus.CARE_HISTORY_UPDATE_OK, "돌봄 이력 수정 성공");
    }

    @Operation(summary = "돌봄 이력 삭제 API", description = "대상자의 돌봄 이력을 삭제합니다.")
    @DeleteMapping("/history/{careHistoryId}")
    public ApiResponse<String> deleteCareHistory(@PathVariable Long careHistoryId) {
        careService.deleteCareHistory(careHistoryId);
        return ApiResponse.of(SuccessStatus.CARE_HISTORY_DELETE_OK, "돌봄 이력 삭제 성공");
    }
}
