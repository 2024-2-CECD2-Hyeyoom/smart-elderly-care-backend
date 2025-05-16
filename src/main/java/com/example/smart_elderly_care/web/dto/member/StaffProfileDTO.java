package com.example.smart_elderly_care.web.dto.member;

import lombok.Data;

@Data
public class StaffProfileDTO {
    private Long staffId;
    private String name;
    private String phone;
    private int welfareCenterId;
}
