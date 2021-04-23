package com.sf.bitcoin.service;

/**
 * 合约逻辑接口
 */
public interface IContractService {
    /**
     * 初始化合约
     */
    void initContract();

    /**
     * 同步合约成交额
     */
    void syncContractTurnover();

    /**
     * 同步合约费率
     */
    void syncContractRate();
}
