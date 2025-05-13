package com.example.smart_elderly_care.service;

import com.example.smart_elderly_care.domain.entity.*;
import com.example.smart_elderly_care.domain.repo.*;
import com.example.smart_elderly_care.exception.CareClientException;
import com.example.smart_elderly_care.exception.code.ErrorStatus;
import com.example.smart_elderly_care.web.dto.care.*;
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
            return userRepository.findByWelfareCenterId(staff.getWelfareCenter().getId());
        } else if (Member.Role.CAREGIVER.equals(member.getRole())) {
            Caregiver caregiver = caregiverRepository.findById(managerId)
                    .orElseThrow(() -> new CareClientException(ErrorStatus.CAREGIVER_NOT_FOUND));
            return caregiver.getElderlyList();
        } else {
            throw new CareClientException(ErrorStatus._FORBIDDEN);
        }
    }

    // User → CareTargetResponseDTO 변환
    private CareTargetDTO mapUserToCareTarget(User user) {
        return CareTargetDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .gender(user.getGender())
                .address(user.getAddress())
                .welfareCenterName(user.getWelfareCenter().getOrganName())
                .phoneNumber(user.getPhone())
                .careStatus(user.getCareStatus())
                .build();
    }

    // CareHistory → CareHistoryResponseDTO 변환
    private CareHistoryDTO.Response mapCareHistoryToResponseDTO(CareHistory history) {
        String userName = userRepository.findById(history.getUserId())
                .map(User::getName)
                .orElse("이름없음");

        String counselorName = staffRepository.findById(history.getCounselorId())
                .map(Staff::getName)
                .orElse("이름없음");

        return CareHistoryDTO.Response.builder()
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
    public List<CareTargetDTO> getCareTargetsByManager(Long userId) {
        List<User> users = getManagedUsers(userId);
        return users.stream()
                .map(this::mapUserToCareTarget)
                .collect(Collectors.toList());
    }

    // 보호자/관리자 대상자 이름만 가져오기
    public List<CareTargetDTO.Name> getCareTargetNamesByManager(Long userId) {
        List<User> users = getManagedUsers(userId);
        return users.stream()
                .map(user -> CareTargetDTO.Name.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public CareTargetDTO.Profile getCareTargetProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CareClientException(ErrorStatus.USER_NOT_FOUND));

        String welfareCenterName = user.getWelfareCenter().getOrganName() != null
                ? user.getWelfareCenter().getOrganName()
                : "정보 없음";

        // 보호자 연락처 추출 (user를 관리 중인 caregiver 중 첫 번째)
        String guardianPhone = caregiverRepository.findAll().stream()
                .filter(caregiver -> caregiver.getElderlyList().stream()
                        .anyMatch(elderly -> elderly.getId().equals(userId)))
                .findFirst()
                .map(Caregiver::getPhone)
                .orElse("보호자 없음");

        return CareTargetDTO.Profile.builder()
                .userId(user.getId())
                .name(user.getName())
                .gender(user.getGender())
                .address(user.getAddress())
                .welfareCenterName(welfareCenterName)
                .phoneNumber(user.getPhone())
                .guardianPhone(guardianPhone)
                .underlyingConditions(user.getUnderlyingDiseases())
                .careStatus(user.getCareStatus())
                .build();
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
    public List<CareHistoryDTO.Response> getCareHistoriesByManager(Long userId) {
        List<User> users = getManagedUsers(userId);
        List<Long> userIds = users.stream().map(User::getId).toList();

        if (userIds.isEmpty()) return List.of();

        List<CareHistory> careHistories = careHistoryRepository.findByUserIdIn(userIds);

        return careHistories.stream()
                .map(this::mapCareHistoryToResponseDTO)
                .collect(Collectors.toList());
    }

    // 노인의 본인 돌봄 이력 조회
    public List<CareHistoryDTO.MyHistory> getCareHistoriesForUser(Long userId) {
        // 사용자 존재 여부 확인
        if (!userRepository.existsById(userId)) {
            throw new CareClientException(ErrorStatus.USER_NOT_FOUND);
        }

        // 해당 사용자의 돌봄 이력 조회
        List<CareHistory> careHistories = careHistoryRepository.findByUserId(userId);

        // 이력 리스트를 DTO로 변환
        return careHistories.stream()
                .map(history -> {
                    String counselorName = staffRepository.findById(history.getCounselorId())
                            .map(Staff::getName)
                            .orElse("이름없음");

                    return CareHistoryDTO.MyHistory.builder()
                            .careHistoryId(history.getId())
                            .visitDate(history.getVisitDate())
                            .purpose(history.getPurpose())
                            .content(history.getContent())
                            .counselorName(counselorName)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 대상자 돌봄 이력 수정
    public void updateCareHistory(Long careHistoryId, CareHistoryDTO.Update dto) {
        // 존재 여부 체크
        CareHistory history = careHistoryRepository.findById(careHistoryId)
                .orElseThrow(() -> new CareClientException(ErrorStatus.CARE_HISTORY_NOT_FOUND));

        if (!userRepository.existsById(dto.getUserId())) {
            throw new CareClientException(ErrorStatus.USER_NOT_FOUND);
        }

        if (!staffRepository.existsById(dto.getCounselorId())) {
            throw new CareClientException(ErrorStatus.STAFF_NOT_FOUND);
        }

        // 담당자가 해당 대상자 관리 중인지 검증
        List<User> managedUsers = getManagedUsers(dto.getCounselorId());
        boolean isManagedUser = managedUsers.stream()
                .anyMatch(user -> user.getId().equals(dto.getUserId()));
        if (!isManagedUser) {
            throw new CareClientException(ErrorStatus._FORBIDDEN);
        }

        // 값 수정
        history.setUserId(dto.getUserId());
        history.setVisitDate(dto.getVisitDate());
        history.setPurpose(dto.getPurpose());
        history.setContent(dto.getContent());
        history.setCounselorId(dto.getCounselorId());

        careHistoryRepository.save(history);
    }

    // 대상자 돌봄 이력 삭제
    public void deleteCareHistory(Long careHistoryId) {
        // 이력 존재 여부 확인
        CareHistory history = careHistoryRepository.findById(careHistoryId)
                .orElseThrow(() -> new CareClientException(ErrorStatus.CARE_HISTORY_NOT_FOUND));

        careHistoryRepository.delete(history);
    }

}
