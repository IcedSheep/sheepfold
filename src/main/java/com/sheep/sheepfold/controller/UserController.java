package com.sheep.sheepfold.controller;

import com.sheep.sheepfold.common.ApiResponse;
import com.sheep.sheepfold.common.ErrorCode;
import com.sheep.sheepfold.common.ResultUtils;
import com.sheep.sheepfold.exception.BusinessException;
import com.sheep.sheepfold.model.dto.UserLoginDTO;
import com.sheep.sheepfold.model.dto.UserLoginVO;
import com.sheep.sheepfold.model.dto.UserRegisterDTO;
import com.sheep.sheepfold.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
//@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ApiResponse<Long> userRegister(@RequestBody UserRegisterDTO param) {
        Long userId = userService.userRegister(param);
        return ResultUtils.success(userId);
    }

    @GetMapping("getCode")
    public ApiResponse<String> getCode(@Param("email") String email) {
        String code = userService.getCode(email);
        return ResultUtils.success(code);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ApiResponse<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String email = userLoginDTO.getUserEmail();
        String userPassword = userLoginDTO.getUserPassword();
        if (StringUtils.isAnyBlank(email, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserLoginVO loginUserVO = userService.userLogin(email, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }
}
