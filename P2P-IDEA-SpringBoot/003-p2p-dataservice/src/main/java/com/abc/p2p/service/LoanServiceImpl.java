package com.abc.p2p.service;

import com.abc.p2p.mapper.LoanInfoMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author yang
 * @Date 2020/10/13 16:43
 * @Description : 投标产品的业务层实现类
 */
@Service(interfaceClass = LoanService.class, timeout = 20000, version = "1.0.0")
@Component
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanInfoMapper loanInfoMapper;

    //查询历史年化平均收益率
    @Override
    public Double queryHistoryAverageRate() {

        Double historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
        return historyAverageRate;
    }
}
