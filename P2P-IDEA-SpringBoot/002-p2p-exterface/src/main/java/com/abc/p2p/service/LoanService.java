package com.abc.p2p.service;

/**
 * @Author yang
 * @Date 2020/10/13 16:18
 * @Description : 投标产品的业务层接口
 */
public interface LoanService {

    /**
     * 查询历史年化平均收益率
     * @return
     */
    Double queryHistoryAverageRate();
}
