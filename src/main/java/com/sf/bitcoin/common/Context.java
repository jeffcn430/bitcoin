package com.sf.bitcoin.common;

import com.sf.bitcoin.service.IContractService;
import com.sf.bitcoin.vo.ContractRateVo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class Context {
    /**
     *
     */
    @Autowired
    private  IContractService contractService;

    /**
     * 合约费率信息
     */
    Map<String, ContractRateVo> ContractRateVos = new HashMap<>();

    @PostConstruct
    public void init() {
        this.contractService.initContract();
    }
}
