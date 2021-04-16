package com.sf.bitcoin.service;

import com.sf.bitcoin.vo.ContractRateVo;

import java.util.List;

/**
 * @author admin
 */
public interface IRateService {
    /**
     * 初始化费率
     */
    void initRate();

    /**
     * 获取费率列表
     *
     * @return 费率列表
     */
    List<ContractRateVo> getRateCountVoList();

    void initContractRate(List<ContractRateVo> contracts);
}