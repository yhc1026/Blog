package com.spring.controller;

import com.spring.api.UserServiceAPI;
import com.spring.api.pojo.UserInfoRegisterRequest;
import com.spring.api.pojo.UserInfoRequest;
import com.spring.api.pojo.UserInfoResponse;
import com.spring.api.pojo.UserLoginResponse;
import com.spring.pojo.Result;
import com.spring.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController implements UserServiceAPI {

    @Autowired
    private UserService userService;

    @Override
    public Result<UserLoginResponse> login(@Validated @RequestBody UserInfoRequest user){
        log.info("用户登录, userName: {}", user.getUserName());
        return Result.success(userService.login(user));
    }

    @Override
    public Result<UserInfoResponse> getUserInfo(@NotNull Integer userId){
        return Result.success(userService.getUserInfo(userId));
    }

    @Override
    public Result<UserInfoResponse> getAuthorInfo(@NotNull Integer blogId){
        return Result.success(userService.selectAuthorInfoByBlogId(blogId));
    }

    @Override
    public Result<Integer> register(@Validated @RequestBody UserInfoRegisterRequest registerRequest) {
        return Result.success(userService.register(registerRequest));
    }
}
