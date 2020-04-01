package com.qianlima.reptile.statistics.job;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 16:48 2020-04-01
 * @Modified By :
 */

import com.qianlima.reptile.statistics.service.PotInformationService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gyx
 * @date 2020-04-01 16:48
 */
@Component
@JobHander(value = "potInformationJobHandler")
public class PotInformationJob extends IJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(InitPublishCountStatisticsJob.class);

    @Resource
    private PotInformationService potInformationService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("potInformationJobHandler excute.");
        try {
            long start = System.currentTimeMillis();
            potInformationService.selectPotInformation();
            logger.info("handle use time in ={}", System.currentTimeMillis() - start);
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
            return ReturnT.FAIL;
        }
    }
}
