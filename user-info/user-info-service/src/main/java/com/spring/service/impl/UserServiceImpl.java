package com.spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.spring.BlogServiceAPI;
import com.spring.api.pojo.UserInfoRegisterRequest;
import com.spring.api.pojo.UserInfoRequest;
import com.spring.api.pojo.UserInfoResponse;
import com.spring.api.pojo.UserLoginResponse;
import com.spring.constants.constant;
import com.spring.convert.BeanConvert;
import com.spring.dataobject.UserInfo;
import com.spring.exception.BlogException;
import com.spring.mapper.UserInfoMapper;
import com.spring.pojo.BlogInfoResponse;
import com.spring.pojo.Result;
import com.spring.service.UserService;
import com.spring.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BlogServiceAPI blogServiceAPI;

    @Autowired
    private BeanConvert beanConvert;

    @Autowired
    private RegexUtil regexUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public UserLoginResponse login(UserInfoRequest user) {
        // redis/MySQL 联合获取用户信息
        UserInfo userInfo = queryUserInfoByName(user.getUserName());
        //验证账号密码是否正确
        if (userInfo==null || userInfo.getId()==null){
            throw new BlogException("用户不存在");
        }
        if (!SecurityUtil.verify(user.getPassword(),userInfo.getPassword())){
            throw new BlogException("用户密码不正确");
        }
        //账号密码正确的逻辑
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userInfo.getId());
        claims.put("name", userInfo.getUserName());
        String jwt = JWTUtils.genJwt(claims);
        return new UserLoginResponse(userInfo.getId(), jwt);
    }

    @Override
    public UserInfoResponse getUserInfo(Integer userId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = selectUserInfoById(userId);
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public UserInfoResponse selectAuthorInfoByBlogId(Integer blogId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();

        //1. 根据博客ID, 获取作者ID
        Result<BlogInfoResponse> blogDeatail = blogServiceAPI.getBlogDeatail(blogId);
        //2. 根据作者ID, 获取作者信息
        if (blogDeatail == null|| blogDeatail.getData()==null){
            throw new BlogException("博客不存在");
        }
        UserInfo userInfo = selectUserInfoById(blogDeatail.getData().getUserId());
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }


    @Override
    public Integer register(UserInfoRegisterRequest registerRequest) {
        // 校验参数
        checkUserInfo(registerRequest);
        // 转换为UserInfo格式
        UserInfo userInfo = beanConvert.convertUserInfo(registerRequest);
        // mysql插入数据
        int result = userInfoMapper.insert(userInfo);
        // redis插入数据
        if(result==1){
            redisUtil.set(redisUtil.buildKey("user", userInfo.getUserName()), JsonUtil.toJson(userInfo), 14*24*60*60L);
            userInfo.setPassword("");
            rabbitTemplate.convertAndSend(constant.USER_EXCHANGE_NAME, "", JsonUtil.toJson(userInfo));
            return userInfo.getId();
        }else{
            throw new BlogException("用户注册失败");
        }
    }


    /**
     * 先从redis中查找，若没命中再去MySQL查找
     * @param userName
     * @return
     */
    public UserInfo queryUserInfoByName(String userName) {
        String key= redisUtil.buildKey("user", userName);
        if (redisUtil.hasKey(key)) {
            String json = redisUtil.get(key);
            UserInfo userInfo = JsonUtil.parseJson(json, UserInfo.class);
            return userInfo;
        }else{
            UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .eq(UserInfo::getUserName, userName).eq(UserInfo::getDeleteFlag, 0));
            if (userInfo!=null){
                redisUtil.set(redisUtil.buildKey("user", userInfo.getUserName()), JsonUtil.toJson(userInfo), 14*24*60*60L);
                return userInfo;
            }
            return null;
        }
    }

    public UserInfo selectUserInfoByName(String userName) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userName).eq(UserInfo::getDeleteFlag, 0));
    }


    private UserInfo selectUserInfoById(Integer userId) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId).eq(UserInfo::getDeleteFlag, 0));
    }


    private void checkUserInfo(UserInfoRegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new BlogException("参数不能为空");
        }
        if (registerRequest.getUserName() == null || registerRequest.getPassword() == null) {
            throw new BlogException("用户名或密码不能为空");
        }
        if (registerRequest.getEmail() == null) {
            throw new BlogException("邮箱不能为空");
        }else{
            log.info("email: "+registerRequest.getEmail());
            if(!regexUtil.isValidEmail(registerRequest.getEmail())){
                throw new BlogException("邮箱格式不正确");
            }
        }
        UserInfo userInfo = selectUserInfoByName(registerRequest.getUserName());
        if (userInfo != null) {
            throw new BlogException("用户名已存在");
        }
    }
}
