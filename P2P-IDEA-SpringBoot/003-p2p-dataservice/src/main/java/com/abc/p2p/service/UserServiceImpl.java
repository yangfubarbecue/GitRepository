package com.abc.p2p.service;

import com.abc.p2p.constants.Constant;
import com.abc.p2p.mapper.FinanceAccountMapper;
import com.abc.p2p.mapper.UserMapper;
import com.abc.p2p.model.FinanceAccount;
import com.abc.p2p.model.User;
import com.abc.p2p.util.DateTimeUtil;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author yang
 * @Date 2020/10/13 21:33
 * @Description : 用户的业务层实现类
 */

@Service(interfaceClass = UserService.class, timeout = 20000, version = "1.0.0")
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    FinanceAccountMapper financeAccountMapper;

    //查询用户数量
    @Override
    public Long countUsers() {
        Long usersNum = userMapper.selectCountUsers();
        return usersNum;
    }

    //根据手机号查询用户
    @Override
    public User checkPhone(String phone) {
        User user = userMapper.selectUserByPhone(phone);
        return user;
    }

    //注册用户
    @Override
    @Transactional
    public User registUser(User user) {
        user.setAddTime(new Date());
        System.out.println(new Date());
        userMapper.insertSelective(user);

        //领888大礼包
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setAvailableMoney(Constant.bigGiftMoney);
        /*
        //用户id是自增长的，因此插入之后才会有id，根据手机号查询用户获取用户id----一般不采用
        user = userMapper.selectUserByPhone(user.getPhone());
        System.out.println("u2"+user);
        */
        /*
        //在 map.xml 中使用 useGeneratedKeys：仅适用于 insert和 update，
        // 这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 法来取出由数据库内部生成的主键
        // （比如：像 MySQL 和 SQL Server 这样的关系型数据库管理系统的自动递增字段），默认值：false。
        //以下是map.xml调用的代码
        // Connection connection;//java.sql
        // connection.prepareStatement("", Statement.RETURN_GENERATED_KEYS);
        */
        financeAccount.setUid(user.getId());
        financeAccountMapper.insertSelective(financeAccount);
        return user;
    }
}
