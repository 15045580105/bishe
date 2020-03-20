package com.qianlima.reptile.statistics.job;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 10:05 2020-03-20
 * @Modified By :
 */

import com.qianlima.reptile.statistics.service.CollectAndReleaseService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gyx
 * @date 2020-03-20 10:05
 */
@Component
@JobHander(value = "collectAndReleasJobHandler")
public class CollectAndReleasJob extends IJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(InitPublishCountStatisticsJob.class);

    @Resource
    private CollectAndReleaseService collectAndReleaseService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("collectAndReleasJobHandler excute.");
        try {
            long start = System.currentTimeMillis();
            collectAndReleaseService.collectAndRelease();
            logger.info("handle use time in ={}",System.currentTimeMillis() - start);
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
            return ReturnT.FAIL;
        }
    }
}
