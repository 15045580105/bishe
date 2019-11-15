package com.qianlima.reptile.statistics.job;

import com.qianlima.reptile.statistics.service.impl.StatisticsServiceImpl;
import com.qianlima.reptile.statistics.task.InitTask;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-15 15:33
 **/
@Component
@JobHander(value = "InitPublishCountStatisticsJobHandler")
public class InitPublishCountStatisticsJob extends IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(InitPublishCountStatisticsJob.class);

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private StatisticsServiceImpl statisticsService;

    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        logger.info("InitPublishCountStatisticsJob excute.");
        if(params.length==0){
            return ReturnT.FAIL;
        }
        for(String timeStr:params){
            try {
                statisticsByParam(timeStr);
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getStackTrace().toString());
                return ReturnT.FAIL;
            }
        }
        return ReturnT.SUCCESS;
    }

    public void statisticsByParam(String timeStr) throws  Exception{
        int startTime=Integer.parseInt(simpleDateFormat.parse(timeStr+" 00:00:00").getTime()/1000+"");
        int endTime = Integer.parseInt(simpleDateFormat.parse(timeStr+" 23:59:59").getTime()/1000+"");
        logger.info("startTime:{},endTime:{}",startTime,endTime);
        statisticsService.statistics(startTime,endTime);
    }
}
