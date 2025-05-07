package com.example.smart_elderly_care.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // todo: 필요한 예외 추가
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // Auth 관련
    AUTH_EXTRACT_ERROR(HttpStatus.UNAUTHORIZED, "AUTH_000", "토큰 추출에 실패했습니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001", "토큰이 만료되었습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "토큰이 유효하지 않습니다."),
    INVALID_LOGIN_REQUEST(HttpStatus.UNAUTHORIZED, "AUTH_003", "올바른 아이디나 패스워드가 아닙니다."),
    NOT_EQUAL_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "리프레시 토큰이 다릅니다."),
    NOT_CONTAIN_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_005", "해당하는 토큰이 저장되어있지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_006", "비밀번호가 일치하지 않습니다."),

    // User 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "존재하지 않는 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_002", "존재하지 않는 사용자(일반 사용자)입니다."),
    CAREGIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_003", "존재하지 않는 사용자(보호자)입니다."),
    STAFF_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_004", "존재하지 않는 사용자(복지센터 담당자)입니다."),
    PHONE_ALREADY_EXISTS(HttpStatus.NOT_FOUND, "USER_005", "이미 등록된 전화번호입니다."),
    USER_DELETE_FAILED(HttpStatus.NOT_FOUND, "USER_006", "회원 탈퇴에 실패했습니다."),
    ELDERLY_CODE_NOT_EXISTS(HttpStatus.NOT_FOUND, "USER_007", "일치하는 고유 코드가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}