package com.sf.bitcoin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 费率展示信息
 *
 * @author admin
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContractRateVo implements Serializable {
    private String code;
    /**
     * 成交量
     */
    private BigDecimal turnover;
    /**
     * 年化利率
     */
    private Map<Integer, Double> rateDayForYear = new HashMap<>();


    public ContractRateVo(String code) {
        this.code = code;
    }
}
