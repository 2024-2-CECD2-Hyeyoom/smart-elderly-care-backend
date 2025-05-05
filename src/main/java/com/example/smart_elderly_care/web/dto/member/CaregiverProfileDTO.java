package com.example.smart_elderly_care.web.dto.member;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CaregiverProfileDTO {
    private Long caregiverId;
    private String name;
    private String phone;
    private List<String> elderlyIds;
    private List<String> elderlyNames;
    private List<String> elderlyPhones;
}
