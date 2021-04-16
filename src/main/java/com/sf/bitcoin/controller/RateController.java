package com.sf.bitcoin.controller;

import com.sf.bitcoin.service.IRateService;
import com.sf.bitcoin.vo.ContractRateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 费率
 *
 * @author admin
 */
@RestController()
public class RateController {
    @Autowired
    private IRateService rateService;

    /**
     * 获取费率统计
     *
     * @return 统一返回信息
     */
    @GetMapping("/coun1t")
    public ResultData getRateCount() {
        List<ContractRateVo> list = rateService.getRateCountVoList();
        return new ResultData(list, list.size());
    }

    /**
     * 重新加载历史记录
     *
     * @return 统一返回信息
     */
    @GetMapping("/init")
    public Result init() {
        rateService.initRate();
        return Result.success();
    }
}
