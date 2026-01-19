package com.spring.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
// 先通过项目中的application yml文件读取到nacos地址，然后再去nacos配置中心读取白名单
@ConfigurationProperties(prefix = "auth.white")
public class AuthWhiteName {
    // 如何赋值：不需要开发者操心，函数已封装
    private List<String> url;
}
