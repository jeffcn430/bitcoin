package com.sf.bitcoin.service;

import com.sf.bitcoin.vo.ContractRateVo;

import java.util.List;

/**
 * @author admin
 */
public interface IContractService {
    /**
     * 初始化合约
     */
    List<ContractRateVo> initContractInfo();
}
