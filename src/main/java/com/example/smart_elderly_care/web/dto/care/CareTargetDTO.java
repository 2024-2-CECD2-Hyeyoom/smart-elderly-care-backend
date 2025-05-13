package com.example.smart_elderly_care.web.dto.care;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class CareTargetDTO {

    private Long userId;
    private String name;
    private int gender;
    private String address;
    private String welfareCenterName;
    private String phoneNumber;
    private int careStatus; // 예: 0 = 정상, 1 = 위험

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Name {
        private Long userId;
        private String name;
    }

    @Data
    @Builder
    public static class Profile {
        private Long userId;
        private String name;
        private int gender;
        private String address;
        private String welfareCenterName;
        private String phoneNumber;
        private String guardianPhone;
        private List<String> underlyingConditions;
        private int careStatus;
    }
}
