package com.sf.bitcoin.repository;

import com.sf.bitcoin.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 费率数据库操作类
 *
 * @author admin
 */
@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    /**
     * 获取指定合约和时间得费率信息
     *
     * @param contractCode 合约编号
     * @param fundingTime  时间
     * @return 合约
     */
    Rate getFirstByContractCodeAndFundingTime(String contractCode, LocalDateTime fundingTime);

    /**
     * 获取指定日期的费率列表
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 费率列表
     */
    @Query(value = "select " +
            " contract_code as contractCode," +
            " date_format(funding_time, '%y-%m-%d') as fundingTime, " +
            " sum(funding_Rate) as fundingRate, " +
            " max(funding_time) " +
            " from Rate " +
            " where funding_Time >= ? and funding_Time < ? " +
            " group by contract_Code, date_format(funding_time, '%y-%m-%d')" +
            " order by funding_Time desc ", nativeQuery = true)
    List<Object[]> getRateList(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
