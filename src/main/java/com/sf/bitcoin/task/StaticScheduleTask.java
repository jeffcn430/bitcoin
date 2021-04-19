package com.sf.bitcoin.task;

import com.sf.bitcoin.service.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * api获取合约费率
 *
 * @author admin
 */
@Configuration
@EnableScheduling
public class StaticScheduleTask {
    @Autowired
    IRateService rateService;
    /**
     * 每分钟获取成交量
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    private void loadContractInfo() {
        rateService.initRate();
    }

    /**
     * 定时获取合约资金费率
     * 0:10, 8:10, 16:10三个时间获取资金费率
     */
    @Scheduled(cron = "0 10 0,8,16 * * ? ")
    private void loadContractRate() {

    }
}
