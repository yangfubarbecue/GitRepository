package com.abc.p2p.mapper;

import com.abc.p2p.model.BidInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    /**
     * 查询累计成交额
     * @return
     */
    Double selectSumBidMoney();
}