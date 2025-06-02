package com.example.smart_elderly_care.web.controller.member;

import com.example.smart_elderly_care.apiPayload.ApiResponse;
import com.example.smart_elderly_care.apiPayload.code.SuccessStatus;
import com.example.smart_elderly_care.service.*;
import com.example.smart_elderly_care.web.dto.member.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;
    private final JoinService joinService;
    private final ProfileService profileService;
    private final WelfareCenterApiService welfareCenterApiService;

    // ---------------------- 로그인 ----------------------
    @PostMapping("/login")
    public ApiResponse<LoginDTO.LoginResponseDTO> login(@RequestBody LoginDTO.LoginRequestDTO request) {
        LoginDTO.LoginResponseDTO dto = loginService.login(request);
        return ApiResponse.of(SuccessStatus.MEMBER_LOGIN_OK, dto);
    }

    // ---------------------- 회원가입 ----------------------
    @Operation(summary = "일반 회원가입 API", description = "일반회원(독거노인) 회원가입입니다.")
    @PostMapping("/signup/user")
    public ApiResponse<String> signupUser(@Valid @RequestBody SignupDTO.UserSignupDTO dto) {
        joinService.signupUser(dto);
        return ApiResponse.of(SuccessStatus.USER_SIGNUP_OK, "일반 회원 가입 성공");
    }

    @Operation(summary = "보호자 회원가입 API", description = "보호자 회원가입입니다.")
    @PostMapping("/signup/caregiver")
    public ApiResponse<String> signupCaregiver(@Valid @RequestBody SignupDTO.CaregiverSignupDTO dto) {
        joinService.signupCaregiver(dto);
        return ApiResponse.of(SuccessStatus.CAREGIVER_SIGNUP_OK, "보호자 회원 가입 성공");
    }

    @Operation(summary = "담당자 회원가입 API", description = "복지센터 담당자 회원가입입니다.")
    @PostMapping("/signup/staff")
    public ApiResponse<String> signupStaff(@Valid @RequestBody SignupDTO.StaffSignupDTO dto) {
        joinService.signupStaff(dto);
        return ApiResponse.of(SuccessStatus.STAFF_SIGNUP_OK, "담당자 회원 가입 성공");
    }

    // ---------------------- 마이페이지 ----------------------
    @Operation(summary = "일반회원 마이페이지 조회 API", description = "일반회원(독거노인) 마이페이지 조회 결과입니다.")
    @GetMapping("/mypage/user/{userId}")
    public ApiResponse<ProfileDTO.UserProfileDTO> getUserProfile(@PathVariable Long userId) {
        ProfileDTO.UserProfileDTO dto = profileService.getUserProfile(userId);
        return ApiResponse.of(SuccessStatus.USER_PROFILE_OK, dto);
    }

    @Operation(summary = "보호자 마이페이지 조회 API", description = "보호자 마이페이지 조회 결과입니다.")
    @GetMapping("/mypage/caregiver/{caregiverId}")
    public ApiResponse<ProfileDTO.CaregiverProfileDTO> getCaregiverProfile(@PathVariable Long caregiverId) {
        ProfileDTO.CaregiverProfileDTO dto = profileService.getCaregiverProfile(caregiverId);
        return ApiResponse.of(SuccessStatus.CAREGIVER_PROFILE_OK, dto);
    }

    @Operation(summary = "담당자 마이페이지 조회 API", description = "담당자 마이페이지 조회 결과입니다.")
    @GetMapping("/mypage/staff/{staffId}")
    public ApiResponse<ProfileDTO.StaffProfileDTO> getStaffProfile(@PathVariable Long staffId) {
        ProfileDTO.StaffProfileDTO dto = profileService.getStaffProfile(staffId);
        return ApiResponse.of(SuccessStatus.STAFF_PROFILE_OK, dto);
    }

    // ---------------------- 복지센터 ----------------------
    @Operation(summary = "복지센터 전체 조회 API", description = "DB에 저장된 모든 복지센터를 조회합니다.")
    @GetMapping("/welfare_centers")
    public ApiResponse<List<WelfareCenterDTO>> getCenters() {
        List<WelfareCenterDTO> welfareCenters = welfareCenterApiService.getWelfareCenters();
        return ApiResponse.of(SuccessStatus.WELFARE_CENTERS_OK, welfareCenters);
    }

    @Operation(summary = "시도 기준 복지센터 필터 API", description = "시도명(sido)으로 복지센터를 조회합니다.")
    @GetMapping("/welfare_centers/sido")
    public ApiResponse<List<WelfareCenterDTO>> getBySido(@RequestParam String sido) {
        List<WelfareCenterDTO> centers = welfareCenterApiService.getWelfareCentersBySido(sido);
        return ApiResponse.of(SuccessStatus.WELFARE_CENTERS_OK, centers);
    }

    @Operation(summary = "시도 + 시군구 기준 복지센터 필터 API", description = "시도명과 시군구명으로 복지센터를 조회합니다.")
    @GetMapping("/welfare_centers/sido-sigungu")
    public ApiResponse<List<WelfareCenterDTO>> getBySidoAndSigungu(@RequestParam String sido, @RequestParam String sigungu) {
        List<WelfareCenterDTO> centers = welfareCenterApiService.getWelfareCentersBySidoAndSigungu(sido, sigungu);
        return ApiResponse.of(SuccessStatus.WELFARE_CENTERS_OK, centers);
    }
}
