package com.sf.bitcoin.enums;

import lombok.Getter;

/**
 * 合约类型
 *
 * @author admin
 */
@Getter
public enum ContractEnum {
    /**
     * 交割合约
     */
    DELIVERY("交割"),
    /**
     * 币本位
     */
    USD("币本位"),
    /**
     * U本位
     */
    USDT("U本位");


    String msg;

    ContractEnum(String msg) {
        this.msg = msg;
    }
}
