package com.qianlima.reptile.statistics.repository;

import com.mongodb.BasicDBObject;
import com.qianlima.reptile.statistics.domain.FaultTmpltStatistics;
import com.qianlima.reptile.statistics.domain.OctopusStatistics;
import com.qianlima.reptile.statistics.domain.TraceStatistic;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

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
    public List<TraceStatistic> queryByTime(String time,Integer type) {

        Criteria criteria = new Criteria().andOperator(
                Criteria.where("queryDate").is(time),
                Criteria.where("type").is(type)
        );
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, TraceStatistic.class);
    }
}
