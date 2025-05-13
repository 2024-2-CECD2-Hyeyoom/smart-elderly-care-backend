package com.example.smart_elderly_care.web.controller.member;

import com.example.smart_elderly_care.apiPayload.ApiResponse;
import com.example.smart_elderly_care.apiPayload.code.SuccessStatus;
import com.example.smart_elderly_care.service.WelfareCenterApiService;
import com.example.smart_elderly_care.web.dto.member.WelfareCenterDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/welfare-centers")
public class WelfareCenterController {

    private final WelfareCenterApiService service;

    @Operation(summary = "복지센터 전체 조회 API", description = "DB에 저장된 모든 복지센터를 조회합니다.")
    @GetMapping
    public ApiResponse<List<WelfareCenterDTO>> getCenters() {
        List<WelfareCenterDTO> welfareCenters = service.getWelfareCenters();
        return ApiResponse.of(SuccessStatus.WELFARE_CENTERS_OK, welfareCenters);
    }

    @Operation(summary = "시도 기준 복지센터 필터 API", description = "시도명(sido)으로 복지센터를 조회합니다.")
    @GetMapping("/sido")
    public ApiResponse<List<WelfareCenterDTO>> getBySido(@RequestParam String sido) {
        List<WelfareCenterDTO> centers = service.getWelfareCentersBySido(sido);
        return ApiResponse.of(SuccessStatus.WELFARE_CENTERS_OK, centers);
    }

    @Operation(summary = "시도 + 시군구 기준 복지센터 필터 API", description = "시도명과 시군구명으로 복지센터를 조회합니다.")
    @GetMapping("/sido-sigungu")
    public ApiResponse<List<WelfareCenterDTO>> getBySidoAndSigungu(@RequestParam String sido, @RequestParam String sigungu) {
        List<WelfareCenterDTO> centers = service.getWelfareCentersBySidoAndSigungu(sido, sigungu);
        return ApiResponse.of(SuccessStatus.WELFARE_CENTERS_OK, centers);
    }
}
