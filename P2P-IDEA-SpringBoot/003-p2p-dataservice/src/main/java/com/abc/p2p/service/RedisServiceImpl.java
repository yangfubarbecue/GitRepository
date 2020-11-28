package com.abc.p2p.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author yang
 * @Date 2020/10/22 8:51
 * @Description : redis 服务
 */
@Service(interfaceClass = RedisService.class, timeout = 20000, version = "1.0.0")
@Component
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key,value,5, TimeUnit.MINUTES);
    }

    @Override
    public String get(String key) {
         return (String)redisTemplate.opsForValue().get(key);
    }
}
