package com.qianlima.reptile.statistics.repository;

import com.mongodb.BasicDBObject;
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
public class OctopusStatisticsRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    public OctopusStatistics save(OctopusStatistics dataStatistics) {
        return mongoTemplate.save(dataStatistics);
    }

    public List<OctopusStatistics> query(OctopusStatistics dataStatistics) {
        Criteria criteria = Criteria.where("queryDate").in(dataStatistics.getQueryDate());
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, OctopusStatistics.class);


    }
}