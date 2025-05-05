package com.example.smart_elderly_care.service;

import com.example.smart_elderly_care.exception.CareClientException;
import com.example.smart_elderly_care.exception.code.ErrorStatus;
import com.example.smart_elderly_care.domain.entity.Caregiver;
import com.example.smart_elderly_care.domain.entity.Staff;
import com.example.smart_elderly_care.domain.entity.User;
import com.example.smart_elderly_care.domain.repo.CaregiverRepository;
import com.example.smart_elderly_care.domain.repo.StaffRepository;
import com.example.smart_elderly_care.domain.repo.UserRepository;

import com.example.smart_elderly_care.web.dto.member.CaregiverProfileDTO;
import com.example.smart_elderly_care.web.dto.member.StaffProfileDTO;
import com.example.smart_elderly_care.web.dto.member.UserProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final CaregiverRepository caregiverRepository;
    private final StaffRepository staffRepository;

    /**
     * 일반 사용자(노인) 프로필 조회
     */
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CareClientException(ErrorStatus.USER_NOT_FOUND));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(user.getId());
        dto.setName(user.getName());
        dto.setGender(user.getGender());
        dto.setBirthDate(java.sql.Date.valueOf(user.getBirthDate()));
        dto.setAddress(user.getAddress());
        dto.setElderlyId(user.getElderlyId());
        dto.setWelfareCenterId(user.getWelfareCenterId().intValue());
        dto.setPhone(user.getPhone());
        dto.setUnderlyingDiseases(user.getUnderlyingDiseases());

        return dto;
    }

    /**
     * 돌봄 제공자 프로필 조회
     */
    public CaregiverProfileDTO getCaregiverProfile(Long caregiverId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new CareClientException(ErrorStatus.USER_NOT_FOUND));

        CaregiverProfileDTO dto = new CaregiverProfileDTO();
        dto.setCaregiverId(caregiver.getId());
        dto.setName(caregiver.getName());
        dto.setPhone(caregiver.getPhone());

        // elderlyList (List<User>) → elderlyId, 이름, 전화번호 추출
        dto.setElderlyIds(caregiver.getElderlyList().stream()
                .map(User::getElderlyId)
                .collect(Collectors.toList()));

        dto.setElderlyNames(caregiver.getElderlyList().stream()
                .map(User::getName)
                .collect(Collectors.toList()));

        dto.setElderlyPhones(caregiver.getElderlyList().stream()
                .map(User::getPhone)
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * 복지 담당자 프로필 조회
     */
    public StaffProfileDTO getStaffProfile(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new CareClientException(ErrorStatus.USER_NOT_FOUND));

        StaffProfileDTO dto = new StaffProfileDTO();
        dto.setStaffId(staff.getId());
        dto.setName(staff.getName());
        dto.setPhone(staff.getPhone());
        dto.setWelfareCenterId(staff.getWelfareCenterId().intValue());

        return dto;
    }
}
