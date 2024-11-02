package com.skku.skkuduler.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {
    //400
    INVALID_FIELD(400, "400_000", "입력 필드가 올바르지 않습니다."),
    ALREADY_SUBSCRIBED(400, "400_001", "이미 구독된 상태입니다."),
    INVALID_UNSUBSCRIPTION(400, "400_002", "구독하지 않은 학과를 구독 취소 할 수 없습니다."),
    DUPLICATE_EMAIL(400,"400_003", "중복된 이메일 입니다."),
    INVALID_URL_PARAMETERS(400, "400_004","잘못된 URL 파라미터 형식입니다"),
    FAILED_FRIEND_REQUEST(400, "400_005", "친구 요청에 실패하였습니다."),
    //401,
    LOGIN_FAILED(401, "401_000", "로그인에 실패하였습니다."),
    PERMISSION_DENIED(401, "401_001", "접근 권한이 없습니다."),
    EXPIRED_TOKEN(401, "401_002", "토큰이 만료 되었습니다"),
    INVALID_TOKEN(401,"401_003","토큰의 서명을 검증 할 수 없습니다."),
    WRONG_TOKEN(401, "401_004", "없거나 형식이 올바르지 않은 토큰 입니다."),
    NOT_SUBSCRIBED(401,"401_005","구독하지 않은 학과입니다."),
    NOT_FRIEND(401,"401_006","친구가 아닙니다."),
    ALREADY_FRIEND(401, "401_007", "이미 친구입니다."),
    NOT_FOUND_FRIENDSHIP(401, "401_408", "해당 친구요청을 찾을 수 없습니다."),
    NOT_PENDING_FRIENDSHIP(401, "401_409", "친구 요청 상태가 요청중이 아닙니다."),

    //404
    EVENT_NOT_FOUND(404,"404_000", "해당 일정을 찾을 수 없습니다."),
    USER_NOT_FOUND(404,"404_001", "해당 유저를 찾을 수 없습니다."),
    DEPARTMENT_NOT_FOUND(404,"404_002" , "해당 학과를 찾을 수 없습니다."),
    CALENDER_NOT_FOUND(404,"404_003", "해당 달력을 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String msg;
}