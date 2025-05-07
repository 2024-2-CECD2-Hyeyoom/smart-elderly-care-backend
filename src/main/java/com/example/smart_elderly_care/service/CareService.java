package com.example.smart_elderly_care.service;

import com.example.smart_elderly_care.domain.entity.*;
import com.example.smart_elderly_care.domain.repo.*;
import com.example.smart_elderly_care.exception.CareClientException;
import com.example.smart_elderly_care.exception.code.ErrorStatus;
import com.example.smart_elderly_care.web.dto.care.CareHistoryDTO;
import com.example.smart_elderly_care.web.dto.care.CareHistoryResponseDTO;
import com.example.smart_elderly_care.web.dto.care.CareTargetResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareService {

    private final CareHistoryRepository careHistoryRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final CaregiverRepository caregiverRepository;
    private final MemberRepository memberRepository;

    // 공통: 관리 대상 User 목록 가져오기
    private List<User> getManagedUsers(Long managerId) {
        Member member = memberRepository.findById(managerId)
                .orElseThrow(() -> new CareClientException(ErrorStatus.MEMBER_NOT_FOUND));

        if (Member.Role.STAFF.equals(member.getRole())) {
            Staff staff = staffRepository.findById(managerId)
                    .orElseThrow(() -> new CareClientException(ErrorStatus.STAFF_NOT_FOUND));
            return userRepository.findByWelfareCenterId(staff.getWelfareCenterId());
        } else if (Member.Role.CAREGIVER.equals(member.getRole())) {
            Caregiver caregiver = caregiverRepository.findById(managerId)
                    .orElseThrow(() -> new CareClientException(ErrorStatus.CAREGIVER_NOT_FOUND));
            return caregiver.getElderlyList();
        } else {
            throw new CareClientException(ErrorStatus._FORBIDDEN);
        }
    }

    // User → CareTargetResponseDTO 변환
    private CareTargetResponseDTO mapUserToCareTarget(User user) {
        return CareTargetResponseDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .gender(user.getGender())
                .address(user.getAddress())
                .welfareCenterId(user.getWelfareCenterId())
                .phoneNumber(user.getPhone())
                .careStatus(user.getCareStatus())
                .build();
    }

    // CareHistory → CareHistoryResponseDTO 변환
    private CareHistoryResponseDTO mapCareHistoryToResponseDTO(CareHistory history) {
        String userName = userRepository.findById(history.getUserId())
                .map(User::getName)
                .orElse("이름없음");

        String counselorName = staffRepository.findById(history.getCounselorId())
                .map(Staff::getName)
                .orElse("이름없음");

        return CareHistoryResponseDTO.builder()
                .careHistoryId(history.getId())
                .userId(history.getUserId())
                .userName(userName)
                .visitDate(history.getVisitDate())
                .purpose(history.getPurpose())
                .content(history.getContent())
                .counselorName(counselorName)
                .build();
    }

    // 보호자/관리자 대상자 조회
    public List<CareTargetResponseDTO> getCareTargetsByManager(Long userId) {
        List<User> users = getManagedUsers(userId);
        return users.stream()
                .map(this::mapUserToCareTarget)
                .collect(Collectors.toList());
    }

    // 대상자의 돌봄 이력 추가
    public void addCareHistory(CareHistoryDTO dto) {
        if (!userRepository.existsById(dto.getUserId())) {
            throw new CareClientException(ErrorStatus.USER_NOT_FOUND);
        }

        if (!staffRepository.existsById(dto.getCounselorId())) {
            throw new CareClientException(ErrorStatus.STAFF_NOT_FOUND);
        }

        List<User> managedUsers = getManagedUsers(dto.getCounselorId());
        boolean isManagedUser = managedUsers.stream()
                .anyMatch(user -> user.getId().equals(dto.getUserId()));
        if (!isManagedUser) {
            throw new CareClientException(ErrorStatus._FORBIDDEN); // 관리 대상자 아님
        }

        CareHistory careHistory = CareHistory.builder()
                .userId(dto.getUserId())
                .visitDate(dto.getVisitDate())
                .purpose(dto.getPurpose())
                .content(dto.getContent())
                .counselorId(dto.getCounselorId())
                .build();

        careHistoryRepository.save(careHistory);
    }

    // 보호자/관리자의 대상자 돌봄 이력 조회
    public List<CareHistoryResponseDTO> getCareHistoriesByManager(Long userId) {
        List<User> users = getManagedUsers(userId);
        List<Long> userIds = users.stream().map(User::getId).toList();

        if (userIds.isEmpty()) return List.of();

        List<CareHistory> careHistories = careHistoryRepository.findByUserIdIn(userIds);

        return careHistories.stream()
                .map(this::mapCareHistoryToResponseDTO)
                .collect(Collectors.toList());
    }
}
