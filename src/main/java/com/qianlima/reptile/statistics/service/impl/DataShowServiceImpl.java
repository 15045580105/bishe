package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.service.DataShowService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        long startTime = MapUtils.getLong(requestParams,"startTime",0L);
        long endTime = MapUtils.getLong(requestParams,"endTime",0L);
        if(startTime==0||endTime==0){
            requestParams.clear();
            requestParams.put("code",4);
            requestParams.put("data",null);
            requestParams.put("msg","startTime或endTime参数缺失");
            return requestParams;
        }
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("updateTime").gte(startTime),Criteria.where("updateTime").lte(endTime));
        Query query = new Query(criteria);
        List<Map> resultList = mongoTemplate.find(query,Map.class,"phpcmsContentStatistics");
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
            String timeStr = MapUtils.getString(dataMap,"updateTimeStr");
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
}
