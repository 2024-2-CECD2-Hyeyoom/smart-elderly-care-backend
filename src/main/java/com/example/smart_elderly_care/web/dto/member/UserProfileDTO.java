package com.example.smart_elderly_care.web.dto.member;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserProfileDTO {
    private Long userId;
    private String name;
    private int gender;
    private Date birthDate;
    private String address;
    private String elderlyId;
    private int welfareCenterId;
    private String phone;
    private List<String> underlyingDiseases;
}
