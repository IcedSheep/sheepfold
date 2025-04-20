package com.sheep.sheepfold.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 */
@Data
public class UserLoginVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;


    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 性别：1-男/2-女
     */
    private Integer gender;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 个人简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}