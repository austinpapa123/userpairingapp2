package com.chuyue.usercenter.common;

/**
 * return stuff utils class
 */
public class ResultUtils {

    /**
     * success
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(0, data, "ok ✔", "");
    }

    /**
     * fail
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    /**
     * fail
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description){
        return new BaseResponse(errorCode, message, description);
    }

    public static BaseResponse error(int errorCode, String message, String description){
        return new BaseResponse(errorCode, message, description);
    }
}
