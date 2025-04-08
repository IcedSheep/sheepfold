package com.sheep.sheepfold.controller;

import com.sheep.sheepfold.common.ApiResponse;
import com.sheep.sheepfold.common.ResultUtils;
import com.sheep.sheepfold.model.dto.UserRegisterDTO;
import com.sheep.sheepfold.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
//@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ApiResponse<Long> userRegister(@RequestBody UserRegisterDTO param) {
        Long userId = userService.userRegister(param);
        return null;
    }

    @GetMapping("getCode")
    public ApiResponse<String> getCode(@Param("email") String email) {
        String code = userService.getCode(email);
        return ResultUtils.success(code);
    }
}
