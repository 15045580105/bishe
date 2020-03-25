package com.qianlima.reptile.statistics.job;

import com.qianlima.reptile.statistics.domain.TraceStatistic;
import com.qianlima.reptile.statistics.repository.TraceStatisticRepository;
import com.qianlima.reptile.statistics.service.TraceStatisticService;
import com.qianlima.reptile.statistics.utils.DateUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: TraceStatisticQueryJob
 * @date 2020/3/19 3:41 下午
 */

@Component
@JobHander(value = "TraceStatisticQueryJobHandler")
public class TraceStatisticQueryJob extends IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(InitPublishCountStatisticsJob.class);

    @Autowired
    private TraceStatisticService traceStatisticService;
    @Autowired
    private TraceStatisticRepository traceStatisticRepository;

    @Override
    public ReturnT<String> execute(String... strings)  {
        logger.info("TraceStatisticQueryJobHandler excute.");
        try {
            long start = System.currentTimeMillis();
            logger.info("handle use time in ={}",System.currentTimeMillis() - start);
            List<TraceStatistic> list = traceStatisticRepository.queryByTime(DateUtils.getFormatDateStrBitAdd(DateUtils.getYesterTodayEndTime(), DateUtils.FUZSDF), 1);
            if (list == null || list.size() == 0) {
                TraceStatisticQuery(DateUtils.getYesterTodayStartTime(),DateUtils.getYesterTodayEndTime());
            }
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
            return ReturnT.FAIL;
        }
    }

    public void TraceStatisticQuery(Long startTime,Long endTime) {
        traceStatisticService.saveStatistic(startTime, endTime);

    }

}
