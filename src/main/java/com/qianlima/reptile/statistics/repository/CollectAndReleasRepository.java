package com.qianlima.reptile.statistics.repository;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 10:08 2020-03-20
 * @Modified By :
 */

import com.mongodb.BasicDBObject;
import com.qianlima.reptile.statistics.domain.CollectAndReleas;
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
 * @date 2020-03-20 10:08
 */
@Repository
public class CollectAndReleasRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    public CollectAndReleas save(CollectAndReleas collectAndReleas) {
        return mongoTemplate.save(collectAndReleas);
    }

    public List<CollectAndReleas> queryByTime(String startTime, String endTime) {
        Criteria criteria = Criteria.where("queryDate").gte(startTime).lte(endTime);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, CollectAndReleas.class);
    }
}
