package com.sf.bitcoin.enums;

import lombok.Getter;

/**
 * @author admin
 */
@Getter
public enum ApiEnum {
    /**
     * 主域名
     */
    mian("https://api.hbdm.com", "https://api.hbdm.com"),
    /** 成交量 */
    TURNOVER("/swap-api/v1/swap_open_interest", "/linear-swap-api/v1/swap_open_interest"),
    /**
     * 合约列表
     */
    CONTRACT_INFO("/swap-api/v1/swap_contract_info", "/linear-swap-api/v1/swap_contract_info"),
    /**
     * 历史费率查询
     */
    HISTORY_RATE("/swap-api/v1/swap_historical_funding_rate", "/linear-swap-api/v1/swap_historical_funding_rate");

    /**
     * 币本位接口
     */
    String usdUrl;
    /**
     * U本位接口
     */
    String usdtUrl;

    ApiEnum(String usdUrl, String usdtUrl) {
        this.usdUrl = usdUrl;
        this.usdtUrl = usdtUrl;
    }
}
