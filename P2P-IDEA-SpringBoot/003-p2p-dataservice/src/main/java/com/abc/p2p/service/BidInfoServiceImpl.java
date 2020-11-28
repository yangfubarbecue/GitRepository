package com.abc.p2p.service;

import com.abc.p2p.mapper.BidInfoMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author yang
 * @Date 2020/10/13 23:17
 * @Description : 用户投标信息业务接口实现类
 */

@Service(interfaceClass = BidInfoService.class, timeout = 20000, version = "1.0.0")
@Component
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    BidInfoMapper bidInfoMapper;

    //查询累计成交额
    @Override
    public Double querySumBidMoney() {
        Double sumBidMoney = bidInfoMapper.selectSumBidMoney();
        return sumBidMoney;
    }
}
