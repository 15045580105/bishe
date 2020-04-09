package com.qianlima.reptile.statistics.job;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 09:30 2020-03-20
 * @Modified By :
 */

import com.qianlima.reptile.statistics.service.TemplateAndPotStatistical;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gyx
 * @date 2020-03-20 09:30
 */
@Component
@JobHander(value = "PotAndTmpltStatisticsjobHandler")
public class PotAndTmpltStatisticsjob extends IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(PotAndTmpltStatisticsjob.class);

    @Resource
    private TemplateAndPotStatistical templateAndPotStatistical;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("PotAndTmpltStatisticsjobHandler excute.");
        try {
            long start = System.currentTimeMillis();
            templateAndPotStatistical.template();
            logger.info("handle use time in ={}",System.currentTimeMillis() - start);
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
            return ReturnT.FAIL;
        }
    }


}
