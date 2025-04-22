package com.sheep.sheepfold.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterDTO implements Serializable {

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;



}
