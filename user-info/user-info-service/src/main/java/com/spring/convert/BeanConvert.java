package com.spring.convert;

import com.spring.api.pojo.UserInfoRegisterRequest;
import com.spring.dataobject.UserInfo;
import com.spring.utils.RegexUtil;
import com.spring.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BeanConvert {

    /**
     * 将注册请求转换为用户信息对象
     * 对密码进行了加密
     * @param registerRequest
     * @return
     */
    public static UserInfo convertUserInfo(UserInfoRegisterRequest registerRequest){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(registerRequest.getUserName());
        userInfo.setPassword(SecurityUtil.encrypt(registerRequest.getPassword()));
        userInfo.setEmail(registerRequest.getEmail());
        userInfo.setGithubUrl(registerRequest.getGithubUrl());
        return userInfo;
    }

}
