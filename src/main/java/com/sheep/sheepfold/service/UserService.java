package com.sheep.sheepfold.service;

import com.sheep.sheepfold.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sheep.sheepfold.model.dto.UserRegisterDTO;

/**
*/
public interface UserService extends IService<User> {

    String getCode(String email);

    Long userRegister(UserRegisterDTO param);



}
