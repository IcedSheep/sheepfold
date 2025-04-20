package com.sheep.sheepfold.service;

import com.sheep.sheepfold.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sheep.sheepfold.model.dto.UserLoginVO;
import com.sheep.sheepfold.model.dto.UserRegisterDTO;

import javax.servlet.http.HttpServletRequest;

/**
*/
public interface UserService extends IService<User> {

    String getCode(String email);

    Long userRegister(UserRegisterDTO param);


    UserLoginVO userLogin(String email, String userPassword, HttpServletRequest request);
}
