package com.sf.bitcoin.repository;

import com.sf.bitcoin.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 合约数据库操作类
 *
 * @author admin
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    /**
     * 通过合约编号获取合约信息
     */
    Contract getContractByContractCode(String code);
}
