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
 * @author liuchanglin
 * @version 1.0
 * @ClassName: OctopusQueryJob
 * @date 2020/3/9 2:25 下午
 */
@Component
@JobHander(value = "OctopusQueryJobHandler")
public class OctopusQueryJob extends IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(InitPublishCountStatisticsJob.class);

    @Resource
    private OctopusMonitorServiceImpl octopusMonitorService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("OctopusQueryJobHandler excute.");
        try {
            octopusQuery();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
            return ReturnT.FAIL;
        }
    }

    public void octopusQuery() {
        octopusMonitorService.saveCurrentDayStatisticsInfo();

    }

}
