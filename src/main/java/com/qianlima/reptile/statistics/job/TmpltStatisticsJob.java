package com.qianlima.reptile.statistics.job;

import com.qianlima.reptile.statistics.service.impl.OctopusMonitorServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@JobHander(value = "tmpltStatisticsJobHandler")
public class TmpltStatisticsJob extends IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(InitPublishCountStatisticsJob.class);

    @Resource
    private OctopusMonitorServiceImpl octopusMonitorService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("tmpltStatisticsJobHandler excute.");
        try {
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
            return ReturnT.FAIL;
        }
    }


}
