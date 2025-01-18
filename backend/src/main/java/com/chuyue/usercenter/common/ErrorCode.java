package com.chuyue.usercenter.common;

/**
 * error code
 * @author chuyue
 */
public enum ErrorCode {
    SUCCESS(0, "all good", ""),
    PARAMS_ERROR(40000, "request parameter error", ""),
    NULL_ERROR(40001, "request data missing", ""),
    NOT_LOGIN(40100, "not log in", ""),
    NO_AUTH(40101, "access denied, not admin", ""),
    INTERNAL_ERROR(50000, "system internal error", "");
    private final int code;

    /**
     * status code info
     */
    private final String message;

    /**
     * status code details
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
