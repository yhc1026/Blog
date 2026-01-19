package com.spring.service;


import com.spring.api.pojo.UserInfoRegisterRequest;
import com.spring.api.pojo.UserInfoRequest;
import com.spring.api.pojo.UserInfoResponse;
import com.spring.api.pojo.UserLoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserLoginResponse login(UserInfoRequest user);

    UserInfoResponse getUserInfo(Integer userId);

    UserInfoResponse selectAuthorInfoByBlogId(Integer blogId);

    Integer register(UserInfoRegisterRequest registerRequest);
}
