package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.entity.PhpcmsContentStatistics;
import com.qianlima.reptile.statistics.job.EveryDayPublishCountStatisticsJob;
import com.qianlima.reptile.statistics.service.QianlimaStatisticsService;
import org.bson.BSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-13 14:14
 **/
@Service
public class StatisticsServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Resource
    private QianlimaStatisticsService qianlimaStatisticsService;

    @Resource
    private MongoTemplate mongoTemplate;

    private String MONGO_COLLECTION_NAME = "phpcmsContentStatistics";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void statistics(int startTime,int endTime){
        int count =  qianlimaStatisticsService.everyDayPublishCount(startTime,endTime);
        int catCount = qianlimaStatisticsService.everyDayCatchCount(startTime,endTime);
        logger.info("phpcmsContentCount:{},biddingRawCount:{}",count,catCount);
        String currentDayStr = simpleDateFormat.format(new Date(startTime*1000L));
        //首先查询是否有当天数据
        Query query = new Query(Criteria.where("currentDayTime").is(startTime));
        long mongoCount = 0;
        try{
            mongoCount = mongoTemplate.count(query,Integer.class,MONGO_COLLECTION_NAME);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        if(mongoCount==0){
            Map<String,Object> mongoMap = new HashMap<>(20);
            mongoMap.put("catchCount",catCount);
            mongoMap.put("publishCount",count);
            mongoMap.put("updateTime",System.currentTimeMillis());
            mongoMap.put("currentDayTime",startTime);
            mongoMap.put("currentDayStr",currentDayStr);
            mongoTemplate.insert(mongoMap,MONGO_COLLECTION_NAME);
            logger.info("{} count is not exist,mongo inserted!",currentDayStr);
        }else{
            Update update = new Update();
            update.set("updateTime",System.currentTimeMillis()).set("publishCount",count).set("catchCount",catCount);
            mongoTemplate.updateFirst(query,update,PhpcmsContentStatistics.class);
            logger.info("{}count is exist,mongo updated!",currentDayStr);
        }
    }

}
