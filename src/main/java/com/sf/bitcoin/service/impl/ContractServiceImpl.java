package com.sf.bitcoin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.bitcoin.common.Context;
import com.sf.bitcoin.service.IContractService;
import com.sf.bitcoin.utils.HttpUtils;
import com.sf.bitcoin.vo.ContractRateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ContractServiceImpl implements IContractService {
    @Autowired
    private Context context;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpUtils httpUtils;

    @Override
    public void initContract() {
        String[] urls = new String[]{"https://api.hbdm.com/linear-swap-api/v1/swap_contract_info"};
        for (String url : urls) {
            String resp = httpUtils.doGet(url);
            JSONObject jsonObject = JSONObject.parseObject(resp);
            if (!jsonObject.containsKey("status") || !"ok".equals(jsonObject.getString("status"))) {
                log.error("初始化合约信息! resp:{}", resp);
                continue;
            }

            JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ContractRateVo vo = ContractRateVo.builder()
                        .contractCode(json.getString("contract_code"))
                        .feeAsset(json.getString("fee_asset"))
                        .build();
                context.getContractRateVos().put(vo.getContractCode(), vo);
            }
        }
        this.syncContractTurnover();
        this.syncContractRate();
        log.info("初始化成功!");
    }

    @Override
    public void syncContractTurnover() {
        String[] urls = new String[]{"https://api.hbdm.com/linear-swap-api/v1/swap_open_interest"};

        for (String url : urls) {
            String resp = httpUtils.doGet(url);
            JSONObject jsonObject = JSONObject.parseObject(resp);
            if (!jsonObject.containsKey("status") || !"ok".equals(jsonObject.getString("status"))) {
                log.error("获取成交量失败! resp:{}", resp);
                continue;
            }
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String code = json.getString("contract_code");
                ContractRateVo vo = context.getContractRateVos().get(code);
                vo.setTurnover(json.getBigDecimal("trade_turnover").setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
            }
        }
    }

    @Override
    public void syncContractRate() {
        Map<String, String> urls = new HashMap<>(2);
        urls.put("USDT", "https://api.hbdm.com/linear-swap-api/v1/swap_funding_rate");

        context.getContractRateVos().values().parallelStream().forEach(o -> {
            String resp = httpUtils.doGet(urls.get(o.getContractCode().split("-")[1]), "contract_code", o.getContractCode());
            JSONObject jsonObject = JSONObject.parseObject(resp);
            if (!jsonObject.containsKey("status") || !"ok".equals(jsonObject.getString("status"))) {
                log.error("获取当前费率错误! resp:{}", resp);
            }
            JSONObject json = jsonObject.getJSONObject("data");
            o.setFundingRate(json.getBigDecimal("funding_rate").multiply(BigDecimal.valueOf(100)).setScale(8, BigDecimal.ROUND_HALF_DOWN).doubleValue());
            o.setEstimatedRate(json.getBigDecimal("estimated_rate").multiply(BigDecimal.valueOf(100)).setScale(8, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        });
    }
}
