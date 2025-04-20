package com.sheep.sheepfold.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新个人信息请求
 *
 * 
 * 
 */
@Data
public class UserUpdateMyDTO implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别：1-男/2-女
     */
    private Integer gender;

    /**
     * 简介
     */
    private String userProfile;

    private static final long serialVersionUID = 1L;
}