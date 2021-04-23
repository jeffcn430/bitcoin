package com.sf.bitcoin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.bitcoin.common.Context;
import com.sf.bitcoin.service.IContractService;
import com.sf.bitcoin.vo.ContractRateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Service
public class ContractServiceImpl implements IContractService {
    @Autowired
    private Context context;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void initContract() {

        this.syncContractTurnover();
        this.syncContractRate();
        log.info("初始化成功!");
    }

    @Override
    public void syncContractTurnover() {
        String[] urls = new String[]{"https://api.hbdm.com/linear-swap-api/v1/swap_open_interest"};

        for (String url : urls) {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            if (responseEntity.getStatusCodeValue() != 200) {
                log.error("http请求错误,resp:{}", responseEntity);
                return;
            }

            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            if (!jsonObject.containsKey("status") || !"ok".equals(jsonObject.getString("status"))) {
                log.error("获取合约成交量失败,json:{}", responseEntity.getBody());
                return;
            }

            JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String code = json.getString("contract_code");
                ContractRateVo vo = context.getContractRateVos().computeIfAbsent(code, k -> ContractRateVo.builder().contractCode(code).build());
                if (vo != null) {
                    vo.setTurnover(json.getBigDecimal("trade_turnover").setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                }
            }
        }
    }

    @Override
    public void syncContractRate() {
        String[] urls = new String[]{"https://api.hbdm.com/linear-swap-api/v1/swap_funding_rate"};
        for (String url : urls) {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            if (responseEntity.getStatusCodeValue() != 200) {
                log.error("http请求错误,resp:{}", responseEntity);
                return;
            }

            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            if (!jsonObject.containsKey("status") || !"ok".equals(jsonObject.getString("status"))) {
                log.error("获取合约成交量失败,json:{}", responseEntity.getBody());
                return;
            }

            JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String code = json.getString("contract_code");
                ContractRateVo vo = context.getContractRateVos().computeIfAbsent(code, k -> ContractRateVo.builder().contractCode(code).build());
                if (vo != null) {
                    vo.setFundingRate(json.getBigDecimal("funding_rate").setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                    vo.setEstimatedRate(json.getBigDecimal("funding_rate").setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                }
            }
        }
    }
}
