package com.spring.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;


@Slf4j
public class RedisUtil {

    private StringRedisTemplate stringRedisTemplate;


    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    /**
     * get key
     * @param key
     * @return
     */
    public String get(String key) {
        try{
            return key==null? null: stringRedisTemplate.opsForValue().get(key);
        }catch (Exception e){
            log.info("Redis get error key: {}", key);
            return null;
        }
    }


    /**
     * 是否存在key
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        try{
            return key==null? false: stringRedisTemplate.hasKey(key);
        }catch (Exception e){
            log.info("Redis hasKey error key: {}", key);
            return false;
        }
    }


    /**
     * set key
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, String value, long timeout) {
        if(timeout < 0){
            log.info("Redis set error timeout: {}", timeout);
            return false;
        }
        if(timeout == 0){
            stringRedisTemplate.opsForValue().set(key, value);
        }
        try{
            stringRedisTemplate.opsForValue().set(key, value, timeout);
            return true;
        }catch (Exception e){
            log.info("Redis set error key: {}", key);
            return false;
        }
    }


    /**
     * 构建key
     * @param obj
     * @return
     */
    public String buildKey(String prefix, Object obj){
        if(obj==null||obj.toString().isEmpty()){
            log.error("buildKey error: obj is empty");
            return null;
        }
        return prefix + ":" + obj.toString();
    }
}
