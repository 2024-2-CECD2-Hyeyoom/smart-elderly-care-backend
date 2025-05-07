package com.example.smart_elderly_care.web.dto.member;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserSignupDTO {
    private String name;
    private String phone;
    private int gender;
    private LocalDate birthDate;
    private String address;
    private Long welfareCenterId;
    private List<String> underlyingDiseases;
    private String password;
}
