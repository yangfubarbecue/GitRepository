package com.abc.p2p.mapper;

import com.abc.p2p.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询用户数量
     * @return
     */
    Long selectCountUsers();

    /**
     * 根据手机号查询用户
     * @return
     */
    User selectUserByPhone(String phone);
}