package com.sheep.sheepfold.common;

/**
 * 返回工具类
 *

 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static ApiResponse error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static ApiResponse error(int code, String message) {
        return new ApiResponse(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static ApiResponse error(ErrorCode errorCode, String message) {
        return new ApiResponse(errorCode.getCode(), null, message);
    }
}
