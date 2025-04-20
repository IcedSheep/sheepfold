package com.sheep.sheepfold.model.dto;
import com.sheep.sheepfold.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求
 *
 * 
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDTO extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userAccount;

    /**
     * 邮箱
     */
    private String userEmail;


    /**
     * 性别：1-男/2-女
     */
    private Integer gender;


    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}