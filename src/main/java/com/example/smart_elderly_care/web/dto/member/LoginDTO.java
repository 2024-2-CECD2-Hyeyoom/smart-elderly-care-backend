package com.example.smart_elderly_care.web.dto.member;

import lombok.Builder;
import lombok.Data;

public class LoginDTO {

    @Data
    public static class LoginRequestDTO {
        private String phone;
        private String password;
    }

    @Data
    @Builder
    public static class LoginResponseDTO {
        private Long memberId;
        private String role; // "USER", "CAREGIVER", "STAFF"
        private String token; // JWT
    }
}
