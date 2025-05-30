package com.sheep.sheepfold.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sheep.sheepfold.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sheep.sheepfold.model.dto.user.UserLoginVO;
import com.sheep.sheepfold.model.dto.user.UserQueryDTO;
import com.sheep.sheepfold.model.dto.user.UserRegisterDTO;

import javax.servlet.http.HttpServletRequest;

/**
*/
public interface UserService extends IService<User> {

    String getCode(String email);

    Long userRegister(UserRegisterDTO param);


    UserLoginVO userLogin(String email, String userPassword, HttpServletRequest request);

    UserLoginVO getLoginUserVO(User user);

    User getLoginUser(HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    QueryWrapper<User> getQueryWrapper(UserQueryDTO userQueryRequest);
}
