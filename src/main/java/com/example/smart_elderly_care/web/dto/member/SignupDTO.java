package com.example.smart_elderly_care.web.dto.member;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SignupDTO {

    @Data
    public static class UserSignupDTO {
        private String name;
        private String phone;
        private int gender;
        private LocalDate birthDate;
        private String address;
        private String welfareCenterName;
        private List<String> underlyingDiseases;
        private String password;
    }

    @Data
    public static class StaffSignupDTO {
        private String name;
        private String phone;
        private int gender;
        private String address;
        private String welfareCenterName;
        private String password;
    }

    @Data
    public static class CaregiverSignupDTO {
        private String name;
        private String phone;
        private int gender;
        private String address;
        private List<String> elderlyIds;
        private String password;
    }
}
