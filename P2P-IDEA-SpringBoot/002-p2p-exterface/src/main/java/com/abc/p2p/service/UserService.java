package com.abc.p2p.service;

import com.abc.p2p.model.User;

/**
 * @Author yang
 * @Date 2020/10/13 21:28
 * @Description : 用户业务层接口
 */
public interface UserService {

    /**
     * 查询用户数量
     * @return
     */
    Long countUsers();

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    User checkPhone(String phone);

    /**
     * 注册用户
     * @param user
     * @return
     */
    User registUser(User user);
}
