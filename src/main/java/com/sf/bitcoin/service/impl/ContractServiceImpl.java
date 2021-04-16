package com.sf.bitcoin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.bitcoin.common.Context;
import com.sf.bitcoin.enums.ApiEnum;
import com.sf.bitcoin.repository.ContractRepository;
import com.sf.bitcoin.repository.RateRepository;
import com.sf.bitcoin.service.IContractService;
import com.sf.bitcoin.utils.HttpClient;
import com.sf.bitcoin.vo.ContractRateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Slf4j
@Service
public class ContractServiceImpl implements IContractService {
    @Autowired
    private Context context;

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private RateRepository rateRepository;

    @Override
    public List<ContractRateVo> initContractInfo() {
        List<ContractRateVo> contracts = getContractByApi(ApiEnum.mian.getUsdUrl() + ApiEnum.TURNOVER.getUsdUrl());
        contracts.addAll(getContractByApi(ApiEnum.mian.getUsdtUrl() + ApiEnum.TURNOVER.getUsdtUrl()));



        return contracts;
    }

    /**
     * 通过api获取合约信息
     *
     * @param api 请求地址
     * @return 合约信息列表
     */
    private List<ContractRateVo> getContractByApi(String api) {
        List<ContractRateVo> list = new ArrayList<>();

        String response = HttpClient.doGet(api);
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            ContractRateVo vo = ContractRateVo.builder()
                    .code(json.getString("contract_code"))
                    .turnover(json.getBigDecimal("trade_turnover").setScale(0, BigDecimal.ROUND_HALF_DOWN))
                    .rateDayForYear(new HashMap<>(10))
                    .build();
            list.add(vo);
        }
        return list;
    }
}

