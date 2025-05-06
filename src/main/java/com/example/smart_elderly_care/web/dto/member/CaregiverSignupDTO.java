package com.example.smart_elderly_care.web.dto.member;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CaregiverSignupDTO {
    private String name;
    private String phone;
    private String gender;
    private String address;
    private List<String> elderlyIds;
    private String password;
}
