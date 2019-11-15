package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.service.DataShowService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public Map<String, Object> dataShow(Map<String, Object> requestParams) {
        List<Map> resultList = getDataByMongo(requestParams);
        if(resultList==null||resultList.size()==0){
            requestParams.clear();
            requestParams.put("code",4);
            requestParams.put("data",null);
            requestParams.put("msg","无数据");
            return requestParams;
        }
        List<String> xData = new ArrayList<>();
        List<Integer> catchData = new ArrayList<>();
        List<Integer> publishData = new ArrayList<>();
        List<Map<String,Object>> dataList = new ArrayList<>();
        Map<String,Object> catchMap = new HashMap<>();
        Map<String,Object> publishMap = new HashMap<>();
        for(Map dataMap:resultList){
            String timeStr = MapUtils.getString(dataMap,"currentDayStr");
            xData.add(timeStr);
            int catchCount = MapUtils.getIntValue(dataMap,"catchCount",0);
            catchData.add(catchCount);
            int publishCount = MapUtils.getIntValue(dataMap,"publishCount",0);
            publishData.add(publishCount);
        }
        catchMap.put("name","catchCount");
        catchMap.put("data",catchData);
        publishMap.put("name","publishCount");
        publishMap.put("data",publishData);
        dataList.add(catchMap);
        dataList.add(publishMap);

        Map resultMap = new HashMap();
        resultMap.put("xData",xData);
        resultMap.put("series",dataList);
        requestParams.clear();
        requestParams.put("code",0);
        requestParams.put("data",resultMap);
        requestParams.put("msg","返回成功");
        return requestParams;
    }

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
        long startTime = MapUtils.getLong(requestParams,"startTime",0L);
        long endTime = MapUtils.getLong(requestParams,"endTime",0L);
        if(startTime==0){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            startTime=c.getTimeInMillis()/1000;
        }
        if(endTime==0){
            endTime=(System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000;
        }
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("currentDayTime").gte(startTime),Criteria.where("currentDayTime").lte(endTime));
        Query query = new Query(criteria);
        return mongoTemplate.find(query,Map.class,"phpcmsContentStatistics");
    }
}
