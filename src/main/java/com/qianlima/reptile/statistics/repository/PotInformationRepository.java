package com.qianlima.reptile.statistics.repository;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 13:52 2020-04-01
 * @Modified By :
 */

import com.mongodb.BasicDBObject;
import com.qianlima.reptile.statistics.domain.PotInformation;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gyx
 * @date 2020-04-01 13:52
 */
@Repository
public class PotInformationRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    public void save(PotInformation potInformation) {
        Query query = Query.query(Criteria.where("pot").is(potInformation.getPot()));
        Update update = Update.update("queryDate", potInformation.getQueryDate()).set("templateNumber", potInformation.getTemplateNumber())
                .set("state", potInformation.getState()).set("collectNumber", potInformation.getCollectNumber()).set("releaseNumber", potInformation.getReleaseNumber())
                .set("createTime", potInformation.getCreateTime()).set("updateTime", potInformation.getUpdateTime()).set("repeatPot", potInformation.getRepeatPot());
        mongoTemplate.upsert(query, update, PotInformation.class);
    }

    /**
     * 分页查询
     *
     * @param
     * @return
     */
    public List<PotInformation> queryByPage(String sortField,String sequence,String potName, long page, int count) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(potName)) {
            criteria = Criteria.where("pot").is(potName);
        }
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.skip(page * count).limit(count);
        if(sequence.equals("desc")){
            query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, sortField)}));
        }else{
            query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.ASC, sortField)}));
        }
        return mongoTemplate.find(query, PotInformation.class);
    }


    public long query() {
        Criteria criteria = new Criteria();
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        return mongoTemplate.count(query, PotInformation.class);
    }


    public List<PotInformation> queryAll() {
        return mongoTemplate.findAll(PotInformation.class);
    }

    public List<PotInformation> queryByIp(String ip,String potName) {
        Criteria criteria = Criteria.where("repeatPot").is(ip);
        criteria.and("pot").ne(potName);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.find(query, PotInformation.class);
    }
    public PotInformation queryByName(String potName) {
        Criteria criteria = Criteria.where("pot").is(potName);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", false);
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "queryDate")}));
        return mongoTemplate.findOne(query, PotInformation.class);
    }


}
