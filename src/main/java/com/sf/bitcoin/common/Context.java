package com.sf.bitcoin.common;

import com.sf.bitcoin.entity.Contract;
import com.sf.bitcoin.repository.ContractRepository;
import com.sf.bitcoin.service.IContractService;
import com.sf.bitcoin.service.IRateService;
import com.sf.bitcoin.vo.ContractRateVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Getter
@Setter
@Component
@Slf4j
public class Context {
    @Autowired
    private IRateService rateService;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private IContractService contractService;

    /**
     * 合约统计信息
     */
    private Map<String, ContractRateVo> contractRateVos = new HashMap<>();

    @PostConstruct
    public void init() {
        this.contractService.initContractInfo();

//        rateService.initRate();
        rateService.initRealRate();

        this.rateService.initContractRate();
        log.info("初始化成功!");
    }
}
