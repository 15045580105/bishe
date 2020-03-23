package com.qianlima.reptile.statistics.repository;

import com.mongodb.BasicDBObject;
import com.qianlima.reptile.statistics.entity.TrendDTO;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @description: 发布量采集量趋势
 * @author: sx
 * @create: 2020-03-18 15:34:28
 **/
@Repository
public class TrendRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 通过日期年月查询
     * @param date 年月
     * @return
     */
    public List<TrendDTO> queryByDate(String date){
        Pattern pattern = Pattern.compile("^" + date + ".*$");
        Criteria criteria = Criteria.where("currentDayStr").regex(pattern);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("_id", false);
        fieldsObject.put("updateTime", false);
        fieldsObject.put("currentDayTime", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.ASC, "currentDayStr")}));
        return mongoTemplate.find(query, TrendDTO.class, "phpcmsContentStatistics");
    }
}
