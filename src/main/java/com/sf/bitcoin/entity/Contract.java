package com.sf.bitcoin.entity;

import com.sf.bitcoin.enums.ContractEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 合约
 *
 * @author admin
 */
@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    /**
     * 品种代码
     */
    private String symbol;
    /**
     * 合约代码
     */
    @Id
    @Column(length = 20)
    private String contractCode;
    /**
     * 合约面值
     */
    private BigDecimal contractSize;
    /**
     * 合约价格最小变动精度
     */
    private BigDecimal priceTick;
    /**
     * 交割时间(毫秒)
     */
    private long deliveryTime;
    /**
     * 合约上市日期
     */
    private String createDate;
    /**
     * 合合约状态: 0:已下市、1:上市、2:待上市、3:停牌，4:待开盘、5:结算中、6:交割中、7:结算完成、8:交割完成
     */
    private int contractStatus;
    /**
     * 合约下次结算时间(毫秒)
     */
    private long settlementDate;
    /**
     * 合约支持的保证金模式 cross：全仓模式；isolated：逐仓模式；all：全逐仓都支持
     */
    private String supportMarginMode;
    /**
     * 合约类型
     */
    private ContractEnum type;
}
