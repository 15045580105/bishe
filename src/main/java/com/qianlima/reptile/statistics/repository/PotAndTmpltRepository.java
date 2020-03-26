package com.qianlima.reptile.statistics.repository;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 15:47 2020-03-19
 * @Modified By :
 */

import com.mongodb.BasicDBObject;
import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;
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
 * @author gyx
 * @date 2020-03-19 15:47
 */
@Repository
public class PotAndTmpltRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    public TmpltAndPotStatistics save(TmpltAndPotStatistics tmpltAndPotStatistics) {
        return mongoTemplate.save(tmpltAndPotStatistics);
    }

    public List<TmpltAndPotStatistics> queryByTimeAndMonth(String startTime, String endTime) {
        Criteria criteria = Criteria.where("queryDate").gte(startTime).lte(endTime).and("endOfTheMonth").is(1);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, TmpltAndPotStatistics.class);
    }

    public TmpltAndPotStatistics queryByTime(String startTime, String endTime) {
        Criteria criteria = Criteria.where("queryDate").gte(startTime).lte(endTime);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.findOne(query, TmpltAndPotStatistics.class);
    }

    public List<TmpltAndPotStatistics> queryByTimeMonth(String startTime, String endTime) {
        Criteria criteria = Criteria.where("queryDate").gte(startTime).lte(endTime);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, TmpltAndPotStatistics.class);
    }
}
