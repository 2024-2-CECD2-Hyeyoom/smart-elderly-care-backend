package com.example.smart_elderly_care.web.dto.member;

import lombok.Data;

import java.util.List;

@Data
public class StaffSignupDTO {
    private String name;
    private String phone;
    private int gender;
    private String address;
    private Long welfareCenterId;
    private String password;
}
