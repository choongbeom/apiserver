package com.cbkim.apiserver.model.code;

public enum ErrorCode {
    // Common
    SUCCESS(0,      "0000", " Success"),
    FAILURE(2001,   "2001", " Failure"),
   
    // Signin
    NOT_USING_ACCOUNT(  2002, "SI01", "사용가능한 계정이 아닙니다."),
    NOT_MATCH_ACCOUNT(  2002, "SI02", "아이디 또는 패스워드가 일치 하지 않습니다."),
    RESTRICTED_ACCOUNT( 2002, "SI03", "해당 계정은 이용제한 상태입니다."),
    DORMANT_ACCOUNT(    2002, "SI04", "해당 계정은 휴면 상태입니다."),
    RESIGN_ACCOUNT(     2002, "SI05", "해당 계정은 탈퇴 또는 탈퇴대기 상태 입니다."),
    FAILURE_SIGNIN(     2002, "SI06", "로그인을 실패 하였습니다."),
    FAILURE_SIGNUP(     2002, "SI07", "회원가입을 실패 하였습니다."),

    // token
    EXPIRED_TOKEN(  9001, "9001", "세션이 만료되었습니다."),
    INVALID_TOKEN(  9001, "9001", "유효하지 않은 접근입니다."),
    NON_SIGNIN(      9001, "9001", "로그인이 필요합니다."),
    ;

    private final String code;
    private final String message;
    private final int status;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    } 
}
