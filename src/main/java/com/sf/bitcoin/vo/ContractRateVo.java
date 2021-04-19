package com.sf.bitcoin.vo;

import com.sf.bitcoin.enums.ContractEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private String contractCode;
    private ContractEnum type;
    /**
     * 成交量
     */
    private BigDecimal turnover;
    /**
     * 本期费率
     */
    private BigDecimal nowRate;
    /**
     * 下期预计费率
     */
    private BigDecimal nextRate;

    /**
     * 统计天数
     */
    private int countDayNum;
    /**
     * 年化利率
     */
    private double rate7DayForYear;
    /**
     * 年化利率
     */
    private double rate30DayForYear;
    /**
     * 年化利率
     */
    private double rate180DayForYear;
    /**
     * 最后统计时间
     */
    private LocalDateTime lastTime;
}
