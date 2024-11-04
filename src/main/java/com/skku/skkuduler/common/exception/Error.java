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
    EVENT_ALREADY_EXISTS(400, "400_005" , "해당 일정이 이미 존재합니다."),
    INVALID_REMOVAL_CALENDER_EVENT(400,"400_006", "해당 일정이 추가되어있지 않아 삭제를 할 수 없습니다."),
    INVALID_DATE_RANGE(400, "400_007", "일정의 시작과 끝이 올바르게 설정되지 않았습니다."),
    FAILED_FRIEND_REQUEST(400, "400_008", "친구 요청에 실패하였습니다."),
    FRIEND_REQUEST_ALREADY_SENT(400, "400_009", "이미 해당 유저에게 친구요청을 보냈습니다."),
    INVALID_FRIEND_REQUEST(400,"400_010","잘못된 친구요청 입니다"),

    //401,
    LOGIN_FAILED(401, "401_000", "로그인에 실패하였습니다."),
    PERMISSION_DENIED(401, "401_001", "접근 권한이 없습니다."),
    EXPIRED_TOKEN(401, "401_002", "토큰이 만료 되었습니다"),
    INVALID_TOKEN(401,"401_003","토큰의 서명을 검증 할 수 없습니다."),
    WRONG_TOKEN(401, "401_004", "없거나 형식이 올바르지 않은 토큰 입니다."),
    NOT_SUBSCRIBED(401,"401_005","구독하지 않은 학과입니다."),
    NOT_FRIEND(401,"401_006","친구가 아닙니다."),
    ALREADY_FRIEND(401, "401_007", "이미 친구입니다."),

    //404
    EVENT_NOT_FOUND(404,"404_000", "해당 일정을 찾을 수 없습니다."),
    USER_NOT_FOUND(404,"404_001", "해당 유저를 찾을 수 없습니다."),
    DEPARTMENT_NOT_FOUND(404,"404_002" , "해당 학과를 찾을 수 없습니다."),
    CALENDER_NOT_FOUND(404,"404_003", "해당 달력을 찾을 수 없습니다."),
    FRIENDSHIP_NOT_FOUND(404,"404_004" , "해당 친구관계를 찾을 수 없습니다."),
    FRIEND_REQUEST_NOT_FOUND(404, "404_005", "해당 친구요청을 찾을 수 없습니다."),
    //500
    FILE_STORE_ERROR(500, "500,000","파일 저장에 실패하였습니다.");

    private final int status;
    private final String code;
    private final String msg;
}
