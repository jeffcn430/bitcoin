package com.sf.bitcoin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.bitcoin.common.Context;
import com.sf.bitcoin.enums.ApiEnum;
import com.sf.bitcoin.enums.ContractEnum;
import com.sf.bitcoin.repository.ContractRepository;
import com.sf.bitcoin.repository.RateRepository;
import com.sf.bitcoin.service.IContractService;
import com.sf.bitcoin.utils.HttpClient;
import com.sf.bitcoin.vo.ContractRateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void initContractInfo() {
        List<ContractRateVo> contracts = getContractByApi(ApiEnum.mian.getUsdUrl() + ApiEnum.TURNOVER.getUsdUrl(), ContractEnum.USD);
        contracts.forEach(o -> context.getContractRateVos().put(o.getContractCode(), o));
        contracts = getContractByApi(ApiEnum.mian.getUsdtUrl() + ApiEnum.TURNOVER.getUsdtUrl(), ContractEnum.USDT);
        contracts.forEach(o -> context.getContractRateVos().put(o.getContractCode(), o));

    }

    /**
     * 通过api获取合约信息
     *
     * @param api 请求地址
     * @return 合约信息列表
     */
    private List<ContractRateVo> getContractByApi(String api, ContractEnum type) {
        List<ContractRateVo> list = new ArrayList<>();

        String response = HttpClient.doGet(api);
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            ContractRateVo vo = ContractRateVo.builder()
                    .contractCode(json.getString("contract_code"))
                    .turnover(json.getBigDecimal("trade_turnover").setScale(0, BigDecimal.ROUND_HALF_DOWN))
                    .type(type)
                    .build();
            list.add(vo);
        }
        return list;
    }
}

