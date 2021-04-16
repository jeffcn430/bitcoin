package com.sf.bitcoin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author admin
 */
@Table
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rate implements Serializable {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue
    @Column
    private Integer id;
    /**
     * 品种代码
     */
    private String symbol;
    /**
     * 合约代码
     */
    private String contractCode;
    /**
     * 资金费币种
     */
    private String feeAsset;
    /**
     * 资金费率时间
     */
    private LocalDateTime fundingTime;
    /**
     * 当期资金费率
     */
    @Column(precision = 21, scale = 19)
    private BigDecimal fundingRate;
    /**
     * 实际资金费率
     */
    @Column(precision = 21, scale = 19)
    private BigDecimal realizedRate;
    /**
     * 平均溢价指数
     */
    @Column(precision = 21, scale = 19)
    private BigDecimal avgPremiumIndex;

    public Rate(String contractCode, LocalDateTime fundingTime, BigDecimal fundingRate) {
        this.contractCode = contractCode;
        this.fundingRate = fundingRate;
        this.fundingTime = LocalDateTime.of(fundingTime.toLocalDate(), LocalTime.of(0,0));
    }
}
