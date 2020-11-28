package com.abc.p2p.mapper;

import com.abc.p2p.model.LoanInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 查询历史年化平均收益率
     * @return
     */
    Double selectHistoryAverageRate();
}