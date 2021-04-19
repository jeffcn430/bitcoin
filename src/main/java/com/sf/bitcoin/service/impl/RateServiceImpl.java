package com.sf.bitcoin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.bitcoin.common.Context;
import com.sf.bitcoin.entity.Rate;
import com.sf.bitcoin.enums.ApiEnum;
import com.sf.bitcoin.enums.ContractEnum;
import com.sf.bitcoin.repository.RateRepository;
import com.sf.bitcoin.service.IRateService;
import com.sf.bitcoin.utils.HttpClient;
import com.sf.bitcoin.vo.ContractRateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author admin
 */
@Slf4j
@Service
public class RateServiceImpl implements IRateService {
    @Autowired
    private Context context;

    @Autowired
    private RateRepository rateRepository;

    @Override
    public void initRate() {
        try {
            ApiEnum api = ApiEnum.HISTORY_RATE;
            context.getContractRateVos().values().parallelStream().forEach(o -> {
                String url = ApiEnum.mian.getUsdUrl();
                url += o.getType() == ContractEnum.USD ? api.getUsdUrl() : api.getUsdtUrl();
                url += "?contract_code=" + o.getContractCode() + "&page_size=50";

                int index = 1;
                while (true) {
                    String response = HttpClient.doGet(url + "&page_index=" + index);
                    JSONObject jsonObject = JSONObject.parseObject(response);
                    if (!"OK".equals(jsonObject.getString("status").toUpperCase())) {
                        log.error("请求服务器错误! code:{}", o.getContractCode());
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    List<Rate> list = JSONArray.parseArray(data.getString("data"), Rate.class);

                    boolean isBreak = false;
                    for (Rate rate : list) {
                        if (rateRepository.getFirstByContractCodeAndFundingTime(rate.getContractCode(), rate.getFundingTime()) == null) {
                            rateRepository.save(rate);
                        } else {
                            isBreak = true;
                        }
                    }

                    index++;
                    if (isBreak || data.getInteger("total_page").equals(data.getInteger("current_page"))) {
                        log.info("{}获取完成,共{}条", o.getContractCode(), data.getInteger("total_size"));
                        break;
                    }
                }
            });
            log.info("获取最新资金费率");
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public List<ContractRateVo> getRateCountVoList() {
        return null;
    }


    @Override
    public void initContractRate() {
        List<Object[]> list = rateRepository.getRateList();
        for (Object[] objects : list) {
            ContractRateVo vo = context.getContractRateVos().get((String) objects[0]);
            vo.setCountDayNum(((BigDecimal) objects[3]).intValue());
            vo.setLastTime(((Timestamp) objects[2]).toLocalDateTime());
            vo.setRate7DayForYear(((BigDecimal) objects[3]).doubleValue());
            vo.setRate30DayForYear(((BigDecimal) objects[4]).doubleValue());
            vo.setRate180DayForYear(((BigDecimal) objects[5]).doubleValue());
        }


    }

//
//    private void setRate(List<ContractRateVo> contractList, LocalDateTime endTime, int dayNum) {
//        List<Object[]> list = rateRepository.getRateList();
//
//        Map<String, ContractRateVo> countVoMap = contractList.stream().collect(Collectors.toMap(ContractRateVo::getContractCode, a -> a, (k1, k2) -> k1));
//
//        Map<Object, List<Object[]>> map1 = list.stream().collect(Collectors.groupingBy(o -> o[0]));
//        map1.forEach((key, value) -> {
//            if (countVoMap.containsKey(key)) {
//                ContractRateVo vo = countVoMap.get(key);
//                double[] rates = value.stream().mapToDouble(o1 -> ((BigDecimal) o1[2]).doubleValue()).toArray();
//
//                double avgRate = getFormatDouble(Arrays.stream(rates).average().orElse(0), 100);
//                vo.getRateDayForYear().put(dayNum, avgRate * 365);
//            }
//        });
//    }
//
//    private double getFormatDouble(double rate, int mul) {
//        return BigDecimal.valueOf(rate).multiply(BigDecimal.valueOf(mul))
//                .setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue();
//    }

    @Override
    public void initRealRate() {
        context.getContractRateVos().values().parallelStream().forEach(o -> {
            String url;
            if (o.getType() == ContractEnum.USD) {
                url = ApiEnum.mian.getUsdUrl() + ApiEnum.NOW_RATE.getUsdUrl();
            } else if (o.getType() == ContractEnum.USDT) {
                url = ApiEnum.mian.getUsdtUrl() + ApiEnum.NOW_RATE.getUsdtUrl();
            } else {
                log.error("合约类型错误!");
                return;
            }


            String response = HttpClient.doGet(url + "?contract_code=" + o.getContractCode());
            JSONObject jsonObject = JSONObject.parseObject(response);
            if (!"OK".equals(jsonObject.getString("status").toUpperCase())) {
                log.error("api获取数据不正确! json:{}", response);
                return;
            }

            JSONObject json = jsonObject.getJSONObject("data");
            o.setNowRate(json.getBigDecimal("funding_rate").multiply(BigDecimal.valueOf(100)).setScale(2, 5));
            o.setNextRate(json.getBigDecimal("estimated_rate").multiply(BigDecimal.valueOf(100)).setScale(2, 5));
        });

    }

    private void initRealRate(String api) {
        String response = HttpClient.doGet(api);
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (!"OK".equals(jsonObject.getString("status").toUpperCase())) {
            log.error("api获取数据不正确! json:{}", response);
            return;
        }

        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Optional<ContractRateVo> voOpt = context.getContractRateVos().values().stream()
                    .filter(o -> o.getContractCode().equals(json.getString("contract_code"))).findFirst();
            if (voOpt.isPresent()) {
                ContractRateVo vo = voOpt.get();
                vo.setNowRate(json.getBigDecimal("funding_rate"));
            }
        }
    }
}
