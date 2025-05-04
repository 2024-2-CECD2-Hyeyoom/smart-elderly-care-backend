package com.example.smart_elderly_care.service;

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

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final CaregiverRepository caregiverRepository;
    private final StaffRepository staffRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // 비밀번호 암호화

    public void signupUser(UserSignupDTO dto) {
        User user = User.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .welfareCenterId(dto.getWelfareCenterId())
                .underlyingDiseases(dto.getUnderlyingDiseases())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Member.Role.USER)
                .build();
        userRepository.save(user);
    }

    public void signupCaregiver(CaregiverSignupDTO dto) {
        Caregiver caregiver = Caregiver.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .elderlyIds(dto.getElderlyIds())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Member.Role.CAREGIVER)
                .build();
        caregiverRepository.save(caregiver);
    }

    public void signupStaff(StaffSignupDTO dto) {
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