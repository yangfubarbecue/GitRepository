package com.abc.p2p.service;

/**
 * @Author yang
 * @Date 2020/10/22 8:38
 * @Description : Redis 服务
 */
public interface RedisService {

    //存值
    public void put(String key, String value);

    //获取值
    public String get(String key);
}
