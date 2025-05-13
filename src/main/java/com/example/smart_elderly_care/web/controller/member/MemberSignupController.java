package com.example.smart_elderly_care.web.controller.member;

import com.example.smart_elderly_care.apiPayload.ApiResponse;
import com.example.smart_elderly_care.apiPayload.code.SuccessStatus;
import com.example.smart_elderly_care.service.JoinService;
import com.example.smart_elderly_care.web.dto.member.SignupDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/signup")
@RequiredArgsConstructor
public class MemberSignupController {

    private final JoinService joinService;

    @Operation(summary = "일반 회원가입 API", description = "일반회원(독거노인) 회원가입입니다.")
    @PostMapping("/user")
    public ApiResponse<String> signupUser(@Valid @RequestBody SignupDTO.UserSignupDTO dto) {
        joinService.signupUser(dto);
        return ApiResponse.of(SuccessStatus.USER_SIGNUP_OK, "일반 회원 가입 성공");
    }

    @Operation(summary = "보호자 회원가입 API", description = "보호자 회원가입입니다.")
    @PostMapping("/caregiver")
    public ApiResponse<String> signupCaregiver(@Valid @RequestBody SignupDTO.CaregiverSignupDTO dto) {
        joinService.signupCaregiver(dto);
        return ApiResponse.of(SuccessStatus.CAREGIVER_SIGNUP_OK, "보호자 회원 가입 성공");
    }

    @Operation(summary = "담당자 회원가입 API", description = "복지센터 담당자 회원가입입니다.")
    @PostMapping("/staff")
    public ApiResponse<String> signupStaff(@Valid @RequestBody SignupDTO.StaffSignupDTO dto) {
        joinService.signupStaff(dto);
        return ApiResponse.of(SuccessStatus.STAFF_SIGNUP_OK, "담당자 회원 가입 성공");
    }
}
