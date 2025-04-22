package com.sheep.sheepfold.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = -5470197019256273913L;
    /**
     * 邮箱
     */
    private String userEmail;


    /**
     * 密码
     */
    private String userPassword;

}
