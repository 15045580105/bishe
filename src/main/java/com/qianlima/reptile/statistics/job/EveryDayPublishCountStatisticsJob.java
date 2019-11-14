package com.qianlima.reptile.statistics.job;

import com.qianlima.reptile.statistics.entity.PhpcmsContentStatistics;
import com.qianlima.reptile.statistics.service.QianlimaStatisticsService;
import com.qianlima.reptile.statistics.service.impl.StatisticsServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-08 17:10
 **/
@Component
@JobHander(value = "EveryDayPublishCountStatisticsJobHandler")
public class EveryDayPublishCountStatisticsJob extends IJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(EveryDayPublishCountStatisticsJob.class);

    @Resource
    private StatisticsServiceImpl  statisticsServiceImpl;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("EveryDayPublishCountStatisticsJob excute.");

        try {
            publishCountStatistics();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
            return ReturnT.FAIL;
        }
    }

    private void publishCountStatistics(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, -24);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int startTime=Integer.valueOf(c.getTimeInMillis()/1000+"");
        int endTime = startTime+86400;
        logger.info("startTime: {},endTime: {}",startTime,endTime);
        statisticsServiceImpl.statistics(startTime,endTime);
    }
}
