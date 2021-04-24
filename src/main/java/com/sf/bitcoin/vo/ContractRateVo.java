package com.sf.bitcoin.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContractRateVo {
    /**
     * 合约编号
     */
    private String contractCode;
    private String feeAsset;
    /**
     * 成交量
     */
    private double turnover;
    private double fundingRate;
    private double estimatedRate;

//    public ContractRateVo(String contractCode) {
//        this.contractCode = contractCode;
//    }
}
