package com.qianlima.reptile.statistics.repository;

import com.mongodb.BasicDBObject;
import com.qianlima.reptile.statistics.domain.OctopusStatistics;
import com.qianlima.reptile.statistics.utils.DateUtils;
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

    public List<OctopusStatistics> queryInTime(String startTime, String endTime) {

        Criteria criteria = Criteria.where("queryDate")
                .andOperator(
                        Criteria.where("createdDate").lte(DateUtils.StringToDate(endTime,DateUtils.FUZSDF)),
                        Criteria.where("createdDate").gte(DateUtils.StringToDate(startTime,DateUtils.FUZSDF)));
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, OctopusStatistics.class);
    }

    public List<OctopusStatistics> queryByTime(String time) {

        Criteria criteria = Criteria.where("queryDate").is(DateUtils.StringToDate(time, DateUtils.FUZSDF));

        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, OctopusStatistics.class);
    }



}