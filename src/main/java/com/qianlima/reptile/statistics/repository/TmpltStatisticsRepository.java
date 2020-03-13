package com.qianlima.reptile.statistics.repository;

import com.mongodb.BasicDBObject;
import com.qianlima.reptile.statistics.domain.FaultTmpltStatistics;
import com.qianlima.reptile.statistics.domain.OctopusStatistics;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TmpltStatisticsRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    public FaultTmpltStatistics save(FaultTmpltStatistics faultTmpltStatistics) {
        return mongoTemplate.save(faultTmpltStatistics);
    }

    public List<OctopusStatistics> queryByTime(String startTime, String endTime, String type) {

        Criteria criteria = Criteria.where("queryDate").gte(startTime).lte(endTime);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, OctopusStatistics.class);
    }


}