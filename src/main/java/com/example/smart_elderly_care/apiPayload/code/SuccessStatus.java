package com.example.smart_elderly_care.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "COMMON", "성공입니다."),
    //TODO: 다양한 성공 응답 추가...

    // 회원 기능 관련
    USER_SIGNUP_OK(HttpStatus.OK, "AUTH2001", "일반 회원 회원가입이 완료되었습니다."),
    CAREGIVER_SIGNUP_OK(HttpStatus.OK, "AUTH2002", "보호자 회원 회원가입이 완료되었습니다."),
    STAFF_SIGNUP_OK(HttpStatus.OK, "AUTH2003", "담당자 회원 회원가입이 완료되었습니다."),
    USER_LOGIN_OK(HttpStatus.OK, "AUTH2004", "회원 로그인이 완료되었습니다."),
    USER_LOGOUT_OK(HttpStatus.OK, "AUTH2005", "회원 로그아웃이 완료되었습니다."),
    USER_DELETE_OK(HttpStatus.OK, "AUTH2006", "회원 탈퇴가 완료되었습니다."),
    USER_REFRESH_OK(HttpStatus.OK, "AUTH2007", "토큰 재발급이 완료되었습니다."),

    // 마이페이지 기능 관련
    USER_PROFILE_OK(HttpStatus.OK, "PROFILE2001", "일반 회원 프로필 조회 성공"),
    CAREGIVER_PROFILE_OK(HttpStatus.OK, "PROFILE2002", "보호자 회원 프로필 조회 성공"),
    STAFF_PROFILE_OK(HttpStatus.OK, "PROFILE2003", "담당자 회원 프로필 조회 성공"),

    // 모니터링 대시보드 기능 관련
    WEEKLY_REPORT_OK(HttpStatus.OK, "REPORT2001", "주간 분석 레포트 조회 성공"),
    SLEEP_ANALYSIS_REPORT_OK(HttpStatus.OK, "REPORT2002", "수면 분석 레포트 조회 성공"),
    OUTING_ANALYSIS_REPORT_OK(HttpStatus.OK, "REPORT2003", "외출 분석 레포트 조회 성공"),
    TEMPERATURE_ANALYSIS_REPORT_OK(HttpStatus.OK, "REPORT2004", "온도 분석 레포트 조회 성공"),
    HUMIDITY_ANALYSIS_REPORT_OK(HttpStatus.OK, "REPORT2005", "습도 분석 레포트 조회 성공"),
    SLEEP_TIME_ANALYSIS_REPORT_OK(HttpStatus.OK, "REPORT2006", "수면 시간별 분석 레포트 조회 성공"),
    OUTING_TIME_ANALYSIS_REPORT_OK(HttpStatus.OK, "REPORT2007", "외출 시간별 분석 레포트 조회 성공"),
    TEMPERATURE_TIME_ANALYSIS_REPORT_OK(HttpStatus.OK, "REPORT2008", "온도 시간별 분석 레포트 조회 성공"),
    HUMIDITY_TIME_ANALYSIS_REPORT_OK(HttpStatus.OK, "REPORT2009", "습도 시간별 분석 레포트 조회 성공")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
