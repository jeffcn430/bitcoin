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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            context.getContracts().parallelStream().forEach(o -> {
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
                    list.parallelStream().forEach(rate -> {
                        if (rateRepository.getFirstByContractCodeAndFundingTime(rate.getContractCode(), rate.getFundingTime()) == null) {
                            rateRepository.save(rate);
                        }
                    });

                    index++;
                    if (data.getInteger("total_page").equals(data.getInteger("current_page"))) {
                        log.info("{}获取完成,共{}条", o.getContractCode(), data.getInteger("total_size"));
                        break;
                    }
                }
            });
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public List<ContractRateVo> getRateCountVoList() {
        return null;
    }


    @Override
    public void initContractRate(List<ContractRateVo> contracts) {
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        this.setRate(contracts, endTime, 7);
        this.setRate(contracts, endTime, 30);
        this.setRate(contracts, endTime, 180);
    }


    private void setRate(List<ContractRateVo> contractList, LocalDateTime endTime, int dayNum) {
        List<Object[]> list = rateRepository.getRateList(endTime.minusDays(dayNum), endTime);

        Map<String, ContractRateVo> countVoMap = contractList.stream().collect(Collectors.toMap(ContractRateVo::getCode, a -> a, (k1, k2) -> k1));

        Map<Object, List<Object[]>> map1 = list.stream().collect(Collectors.groupingBy(o -> o[0]));
        map1.forEach((key, value) -> {
            if (countVoMap.containsKey(key)) {
                ContractRateVo vo = countVoMap.get(key);
                double[] rates = value.stream().mapToDouble(o1 -> ((BigDecimal) o1[2]).doubleValue()).toArray();

                double avgRate = getFormatDouble(Arrays.stream(rates).average().orElse(0), 100);
                vo.getRateDayForYear().put(dayNum, avgRate * 365);
            }
        });
    }

    private double getFormatDouble(double rate, int mul) {
        return BigDecimal.valueOf(rate).multiply(BigDecimal.valueOf(mul))
                .setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }
}
