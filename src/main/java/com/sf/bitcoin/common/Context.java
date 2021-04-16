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
import java.util.List;

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
     * 合约列表
     */
    private List<Contract> contracts;
    /**
     * 合约统计信息
     */
    private List<ContractRateVo> contractRateVos;

    @PostConstruct
    public void init() {
        this.contractRateVos = this.contractService.initContractInfo();
        this.rateService.initContractRate(this.contractRateVos);
        log.info("初始化成功!");
    }
}
