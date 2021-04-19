package com.sf.bitcoin.controller;

import com.sf.bitcoin.common.Context;
import com.sf.bitcoin.service.IRateService;
import com.sf.bitcoin.vo.ContractRateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 费率
 *
 * @author admin
 */
@RestController()
public class ContractController {
    @Autowired
    Context context;
    @Autowired
    private IRateService rateService;

    /**
     * 获取费率统计
     *
     * @return 费率统计
     */
    @GetMapping("count")
    public ResultData getRateCount(Long turnover) {
        List<ContractRateVo> list = context.getContractRateVos().values().stream()
                .filter(o->o.getTurnover().doubleValue() > turnover)
                .collect(Collectors.toList());
        return new ResultData(list);
    }
}
