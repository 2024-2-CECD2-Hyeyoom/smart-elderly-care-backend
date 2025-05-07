package com.example.smart_elderly_care.web.dto.care;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CareTargetResponseDTO {
    private Long userId;
    private String name;
    private int gender;
    private String address;
    private Long welfareCenterId;
    private String phoneNumber;
    private int careStatus; // 예: 0 = 정상, 1 = 위험
}
