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
     * 获取年化费率
     *
     * @return 费率列表
     */
    @Query(value = "select contract_code, " +
            "           cast(count(1) as DECIMAL) count, " +
            "           max(funding_time) lastTime," +
            "           CAST(avg(case WHEN funding_time >= date_sub(now(),interval 7 day) then funding_rate end)*100*3*365 as decimal(38, 2)) as day7ForYear,\n" +
            "           CAST(avg(case WHEN funding_time >= date_sub(now(),interval 30 day) then funding_rate end)*100*3*365 as decimal(38, 2)) as day30ForYear,\n" +
            "           CAST(avg(case WHEN funding_time >= date_sub(now(),interval 180 day) then funding_rate end)*100*3*365 as decimal(38, 2)) as day180ForYear\n" +
            "       from rate " +
            "       group by contract_code " +
            "       order by funding_time desc", nativeQuery = true)
    List<Object[]> getRateList();
}
