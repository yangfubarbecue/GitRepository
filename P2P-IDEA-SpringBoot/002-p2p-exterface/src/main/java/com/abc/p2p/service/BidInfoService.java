package com.abc.p2p.service;

/**
 * @Author yang
 * @Date 2020/10/13 23:12
 * @Description : 用户投标信息业务接口
 */
public interface BidInfoService {

    /**
     * 查询累计成交额
     * @return
     */
    Double querySumBidMoney();
}
