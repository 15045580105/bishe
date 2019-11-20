package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.service.DataShowService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-11 13:56
 **/
@Service
public class DataShowServiceImpl implements DataShowService {

    @Resource
    private MongoTemplate mongoTemplate;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public Map<String, Object> dataDisplay(Map<String, Object> requestParams) {
        List<Map> resultList = getDataByMongo(requestParams);
        if(resultList==null||resultList.size()==0){
            requestParams.clear();
            requestParams.put("code",4);
            requestParams.put("data",null);
            requestParams.put("msg","无数据");
            return requestParams;
        }
        for(Map<String,Object> map:resultList){
            map.remove("_id");
        }
        requestParams.clear();

        requestParams.put("code",0);
        requestParams.put("data",resultList);
        requestParams.put("msg","返回成功");
        return requestParams;
    }

    private List<Map> getDataByMongo(Map<String, Object> requestParams){
        String startTimeStr = MapUtils.getString(requestParams,"startTime");
        String endTimeStr = MapUtils.getString(requestParams,"endTime");
        long startTime = 0;
        long endTime = 0;
        try{
            startTime = simpleDateFormat.parse(startTimeStr).getTime();
        }catch (Exception e){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            startTime=c.getTimeInMillis()/1000;
        }
        try{
            endTime = simpleDateFormat.parse(endTimeStr).getTime();
        }catch (Exception e){
            endTime=(System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000;
        }
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("currentDayTime").gte(startTime),Criteria.where("currentDayTime").lte(endTime));
        Query query = new Query(criteria);
        return mongoTemplate.find(query,Map.class,"phpcmsContentStatistics");
    }
}
