package com.example.smart_elderly_care.web.dto.member;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProfileDTO {

    @Data
    public static class UserProfileDTO {
        private Long userId;
        private String name;
        private int gender;
        private Date birthDate;
        private String address;
        private String elderlyId;
        private String welfareCenterName;
        private String phone;
        private List<String> underlyingDiseases;
    }

    @Data
    public static class StaffProfileDTO {
        private Long staffId;
        private String name;
        private String phone;
        private String welfareCenterName;
    }

    @Data
    public static class CaregiverProfileDTO {
        private Long caregiverId;
        private String name;
        private String phone;
        private List<String> elderlyIds;
        private List<String> elderlyNames;
        private List<String> elderlyPhones;
    }
}
