package com.qianlima.reptile.statistics.job;

import com.qianlima.reptile.statistics.service.TraceStatisticService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("TraceStatisticQueryJobHandler excute.");
        try {
            TraceStatisticQuery();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
            return ReturnT.FAIL;
        }
    }

    public void TraceStatisticQuery() {
        traceStatisticService.saveStatistic();

    }

}
