package com.example.smart_elderly_care.service;

import com.example.smart_elderly_care.exception.CareClientException;
import com.example.smart_elderly_care.exception.code.ErrorStatus;
import com.example.smart_elderly_care.domain.entity.*;
import com.example.smart_elderly_care.domain.repo.UserRepository;
import com.example.smart_elderly_care.domain.repo.CaregiverRepository;
import com.example.smart_elderly_care.domain.repo.StaffRepository;
import com.example.smart_elderly_care.web.dto.member.CaregiverSignupDTO;
import com.example.smart_elderly_care.web.dto.member.StaffSignupDTO;
import com.example.smart_elderly_care.web.dto.member.UserSignupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final CaregiverRepository caregiverRepository;
    private final StaffRepository staffRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // 비밀번호 암호화

    public void signupUser(UserSignupDTO dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new CareClientException(ErrorStatus.PHONE_ALREADY_EXISTS);
        }

        String generatedElderlyId = UUID.randomUUID().toString();

        User user = User.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .welfareCenterId(dto.getWelfareCenterId())
                .underlyingDiseases(dto.getUnderlyingDiseases())
                .elderlyId(generatedElderlyId)
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .careStatus(0) // 기본 0이 정상
                .role(Member.Role.USER)
                .build();
        userRepository.save(user);
    }

    public void signupCaregiver(CaregiverSignupDTO dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new CareClientException(ErrorStatus.PHONE_ALREADY_EXISTS);
        }

        Caregiver caregiver = Caregiver.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Member.Role.CAREGIVER)
                .build();

        if (dto.getElderlyIds() != null && !dto.getElderlyIds().isEmpty()) {
            List<User> elderlyList = userRepository.findByElderlyIdIn(dto.getElderlyIds());

            // 검증: 입력된 elderlyIds와 DB 조회된 elderlyId 수 비교
            if (elderlyList.size() != dto.getElderlyIds().size()) {
                throw new CareClientException(ErrorStatus.ELDERLY_CODE_NOT_EXISTS);
            }

            caregiver.setElderlyList(elderlyList);
        }

        caregiverRepository.save(caregiver);
    }


    public void signupStaff(StaffSignupDTO dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new CareClientException(ErrorStatus.PHONE_ALREADY_EXISTS);
        }

        Staff staff = Staff.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .welfareCenterId(dto.getWelfareCenterId())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Member.Role.STAFF)
                .build();
        staffRepository.save(staff);
    }
}