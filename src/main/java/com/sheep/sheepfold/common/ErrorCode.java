package com.sheep.sheepfold.common;

/**
 * 自定义错误码
 *

 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(4000, "请求参数错误"),
    NOT_LOGIN_ERROR(4010, "未登录"),
    NO_AUTH_ERROR(4020, "无权限"),
    SYSTEM_ERROR(5000, "系统内部异常"),
    OPERATION_ERROR(5001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
