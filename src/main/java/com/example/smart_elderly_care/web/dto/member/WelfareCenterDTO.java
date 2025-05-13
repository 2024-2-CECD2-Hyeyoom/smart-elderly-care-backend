package com.example.smart_elderly_care.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WelfareCenterDTO {
    private String sido;
    private String sigungu;
    private String organName;
    private String address;
    private String phone;
    private String bizType;
    private String organType;
}