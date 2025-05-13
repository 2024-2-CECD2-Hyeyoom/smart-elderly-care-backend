package com.example.smart_elderly_care.web.controller.member;

import com.example.smart_elderly_care.apiPayload.ApiResponse;
import com.example.smart_elderly_care.apiPayload.code.SuccessStatus;
import com.example.smart_elderly_care.service.ProfileService;
import com.example.smart_elderly_care.web.dto.member.ProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/mypage")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 일반 사용자(노인) 마이페이지 조회
     */
    @Operation(summary = "일반회원 마이페이지 조회 API", description = "일반회원(독거노인) 마이페이지 조회 결과입니다.")
    @GetMapping("/user/{userId}")
    public ApiResponse<ProfileDTO.UserProfileDTO> getUserProfile(@PathVariable Long userId) {
        ProfileDTO.UserProfileDTO dto = profileService.getUserProfile(userId);
        return ApiResponse.of(SuccessStatus.USER_PROFILE_OK, dto);
    }

    /**
     * 돌봄 제공자 마이페이지 조회
     */
    @Operation(summary = "보호자 마이페이지 조회 API", description = "보호자 마이페이지 조회 결과입니다.")
    @GetMapping("/caregiver/{caregiverId}")
    public ApiResponse<ProfileDTO.CaregiverProfileDTO> getCaregiverProfile(@PathVariable Long caregiverId) {
        ProfileDTO.CaregiverProfileDTO dto = profileService.getCaregiverProfile(caregiverId);
        return ApiResponse.of(SuccessStatus.CAREGIVER_PROFILE_OK, dto);
    }

    /**
     * 복지 담당자 마이페이지 조회
     */
    @Operation(summary = "담당자 마이페이지 조회 API", description = "담당자 마이페이지 조회 결과입니다.")
    @GetMapping("/staff/{staffId}")
    public ApiResponse<ProfileDTO.StaffProfileDTO> getStaffProfile(@PathVariable Long staffId) {
        ProfileDTO.StaffProfileDTO dto = profileService.getStaffProfile(staffId);
        return ApiResponse.of(SuccessStatus.STAFF_PROFILE_OK, dto);
    }
}
