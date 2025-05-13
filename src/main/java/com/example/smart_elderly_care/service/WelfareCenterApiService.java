package com.example.smart_elderly_care.service;

import com.example.smart_elderly_care.domain.entity.WelfareCenter;
import com.example.smart_elderly_care.domain.repo.WelfareCenterRepository;
import com.example.smart_elderly_care.web.dto.member.WelfareCenterDTO;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WelfareCenterApiService {

    private final WebClient webClient;
    private final WelfareCenterRepository repository;

    @Value("${data.go.kr.service-key}")
    private String serviceKey;

    // 복지센터 외부 API 동기화
    public void syncWelfareCenters() {
        String baseUrl = "http://apis.data.go.kr/1352000/ODMS_EMG_02/callEmg02Api";

        try {
            String uri = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("numOfRows", 500)
                    .queryParam("pageNo", 1)
                    .queryParam("apiType", "JSON")
                    .build(false).toUriString();

            String response = webClient.get()
                    .uri(uri)
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept", "application/json")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONArray dataArray = new JSONObject(response).getJSONArray("items");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject obj = dataArray.getJSONObject(i);
                String name = obj.optString("organNm");

                if (!repository.existsByOrganName(name)) {
                    WelfareCenter center = WelfareCenter.builder()
                            .sido(obj.optString("sido"))
                            .sigungu(obj.optString("sigungu"))
                            .organName(name)
                            .address(obj.optString("organAddr"))
                            .phone(obj.optString("organTel"))
                            .bizType(obj.optString("bzType"))
                            .organType(obj.optString("organType"))
                            .build();
                    repository.save(center);
                }
            }
        } catch (Exception e) {
            log.error("예외 발생", e);
        }
    }

    // DB에서 모든 복지센터 가져오기
    public List<WelfareCenterDTO> getWelfareCenters() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<WelfareCenterDTO> getWelfareCentersBySido(String sido) {
        return repository.findBySido(sido).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<WelfareCenterDTO> getWelfareCentersBySidoAndSigungu(String sido, String sigungu) {
        return repository.findBySidoAndSigungu(sido, sigungu).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 공통 DTO 매핑 메서드
    private WelfareCenterDTO toDTO(WelfareCenter center) {
        return WelfareCenterDTO.builder()
                .sido(center.getSido())
                .sigungu(center.getSigungu())
                .organName(center.getOrganName())
                .address(center.getAddress())
                .phone(center.getPhone())
                .bizType(center.getBizType())
                .organType(center.getOrganType())
                .build();
    }
}

