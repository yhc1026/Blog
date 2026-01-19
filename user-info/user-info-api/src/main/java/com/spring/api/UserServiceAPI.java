package com.spring.api;


import com.spring.api.pojo.UserInfoRegisterRequest;
import com.spring.api.pojo.UserInfoRequest;
import com.spring.api.pojo.UserInfoResponse;
import com.spring.api.pojo.UserLoginResponse;
import com.spring.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="user-service", path = "/user")
public interface UserServiceAPI {

    @RequestMapping("/login")
    Result<UserLoginResponse> login(@RequestBody UserInfoRequest user);

    @RequestMapping("/getUserInfo")
    Result<UserInfoResponse> getUserInfo(@RequestParam("userId") Integer userId);

    @RequestMapping("/getAuthorInfo")
    Result<UserInfoResponse> getAuthorInfo(@RequestParam("blogId") Integer blogId);

    @RequestMapping("/register")
    Result<Integer> register(@RequestBody UserInfoRegisterRequest registerRequest);
}
