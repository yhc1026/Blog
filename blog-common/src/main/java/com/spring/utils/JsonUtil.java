package com.spring.utils;


import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil<T> {

    /**
     * 字符串转对象
     */
    public static <T> T parseJson(String json, Class<T> clazz){
        if(json==null||json.isEmpty()||clazz==null){
            return null;
        }try{
            return JSON.parseObject(json, clazz);
        }catch(Exception e){
            log.error("json转换错误",e);
            return null;
        }
    }


    /**
     * 对象转字符串
     */
    public static String toJson(Object obj){
        if(obj == null){
            return null;
        }
        try {
            return JSON.toJSONString(obj);
        }catch (Exception e){
            log.error("json转换错误",e);
            return null;
        }
    }

}


