package com.yongxv.bishe.zhanshi.repository;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 10:08 2020-03-20
 * @Modified By :
 */

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import com.yongxv.bishe.zhanshi.domain.Goods;
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
 * @date 2020-03-20 10:08
 */
@Repository
public class GoodsRepository {
    @Resource
    private MongoTemplate mongoTemplate;


    public void save(Goods goods){
        Query query = Query.query(Criteria.where("picture").is(goods.getPicture()));
        Update update = Update.update("uid",goods.getUid()).set("price", goods.getPrice())
                .set("introduction", goods.getIntroduction()).set("type",goods.getType()).set("name", goods.getName())
                .set("storeType", goods.getStoreType()).set("createTime", goods.getCreateTime()).set("updateTime",goods.getUpdateTime());
        mongoTemplate.upsert(query, update,Goods.class);
    }



    public List<Goods> queryByUid(String uid,Integer page,Integer count) {
        Criteria criteria = Criteria.where("uid").is(uid);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.skip(page * count).limit(count);
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "uid")}));
        return mongoTemplate.find(query, Goods.class);
    }


    public List<Goods> queryGoods(Integer page,Integer count) {
        Criteria criteria = new Criteria();
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.skip(page * count).limit(count);
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "creatTime")}));
        return mongoTemplate.find(query, Goods.class);
    }

    public Goods queryById(String id) {
        Criteria criteria = Criteria.where("id").is(id);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "uid")}));
        return mongoTemplate.findOne(query, Goods.class);
    }


    public Goods queryOneByUid(String uid) {
        Criteria criteria = Criteria.where("uid").is(uid);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        query.with(new Sort(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "uid")}));
        return mongoTemplate.findOne(query, Goods.class);
    }

    public long query(String storeType) {
        Criteria criteria = new Criteria().where("storeType").is(storeType);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        return mongoTemplate.count(query, Goods.class);
    }

    public long queryCount(String uid) {
        Criteria criteria = Criteria.where("uid").is(uid);
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        return mongoTemplate.count(query, Goods.class);
    }

    public long queryAllCount() {
        Criteria criteria = new Criteria();
        Document document = criteria.getCriteriaObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        Query query = new BasicQuery(document.toJson(), fieldsObject.toJson());
        return mongoTemplate.count(query, Goods.class);
    }


    public void update(Goods goods) {
        Query query = Query.query(Criteria.where("id").is(goods.getId()));
        Update update = Update.update("name",goods.getName()).set("introduction",goods.getIntroduction())
                .set("price",goods.getPrice()).set("updateTime",goods.getUpdateTime());
        mongoTemplate.upsert(query, update, Goods.class);
    }


    public DeleteResult delete(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.remove(query, Goods.class);
    }
}
