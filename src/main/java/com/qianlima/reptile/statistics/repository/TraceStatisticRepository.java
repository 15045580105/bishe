package com.qianlima.reptile.statistics.repository;

import com.mongodb.BasicDBObject;
import com.qianlima.reptile.statistics.domain.FaultTmpltStatistics;
import com.qianlima.reptile.statistics.domain.OctopusStatistics;
import com.qianlima.reptile.statistics.domain.TraceStatistic;
import com.qianlima.reptile.statistics.entity.PhpcmsContentStatistics;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: TraceStatisticRepository
 * @date 2020/3/18 12:06 上午
 */
@Repository
public class TraceStatisticRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    public TraceStatistic save(TraceStatistic TraceStatistic) {
        return mongoTemplate.save(TraceStatistic);
    }

    public List<TraceStatistic> queryInTime(String startTime, String endTime,Integer type) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("queryDate").gte(startTime).lte(endTime),
                Criteria.where("type").is(type)
        );
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, TraceStatistic.class);
    }

    public List<TraceStatistic> queryInTime(String startTime, String endTime) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("queryDate").gte(startTime).lte(endTime)
        );
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, TraceStatistic.class);
    }

    public List<TraceStatistic> queryByTime(String time,Integer type) {
        Criteria criteria = Criteria.where("queryDate").is(time).and("type").is(type);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, TraceStatistic.class);
    }

    public Map<String, Integer> queryEachTotalCountByTime(String date) {
        Criteria criteria = Criteria.where("currentDayStr").is(date);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        PhpcmsContentStatistics pc =  mongoTemplate.findOne(query, PhpcmsContentStatistics.class);
        HashMap<String, Integer> result = new HashMap<>();
        if (pc != null){
            result.put("发布量", pc.getPublishCount());
            result.put("采集量", pc.getCatchCount());
        } else {
            result.put("发布量", 0);
            result.put("采集量", 0);
        }
        return result;
    }

    public Map<String, Integer> queryEachTotalCountInTime(String startTime, String endTime) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("currentDayStr").gte(startTime).lte(endTime)
        );
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        List<PhpcmsContentStatistics> phpcmsContentStatisticsList =  mongoTemplate.find(query, PhpcmsContentStatistics.class);
        int publishCount = 0;
        int catchCount = 0;
        for (PhpcmsContentStatistics phpcmsContentStatistics : phpcmsContentStatisticsList) {
            publishCount += phpcmsContentStatistics.getPublishCount();
            catchCount += phpcmsContentStatistics.getCatchCount();
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("发布量", publishCount);
        result.put("采集量", catchCount);
        return result;
    }

}
