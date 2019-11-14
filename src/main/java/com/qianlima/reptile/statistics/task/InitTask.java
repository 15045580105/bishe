package com.qianlima.reptile.statistics.task;

import com.qianlima.reptile.statistics.job.EveryDayPublishCountStatisticsJob;
import com.qianlima.reptile.statistics.service.QianlimaStatisticsService;
import com.qianlima.reptile.statistics.service.impl.StatisticsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-13 13:58
 **/
@Component
public class InitTask {
    private static final Logger logger = LoggerFactory.getLogger(InitTask.class);

    @Resource
    private StatisticsServiceImpl statisticsService;

    private int DAY_SECONDS = 86400;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(fixedDelay = 86400*1000)
    public void initTaskStart(){
        //初始化需要把本月至今的数据量全部统计一下
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int startTime=Integer.valueOf(c.getTimeInMillis()/1000+"");
        //获取运行时零点秒数
        long currentTime=(System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000;
        for(;startTime<currentTime;startTime=startTime+DAY_SECONDS){
            int endTime = startTime+DAY_SECONDS;
            logger.info("startTime:{},endTime:{}",startTime,endTime);
            statisticsService.statistics(startTime,endTime);
        }
    }
}
