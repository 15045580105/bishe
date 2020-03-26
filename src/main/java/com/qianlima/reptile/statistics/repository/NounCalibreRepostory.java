package com.qianlima.reptile.statistics.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.qianlima.reptile.statistics.domain.NounCalibre;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;


import javax.annotation.Resource;

/**
 * @author NounCalibre
 * @version 1.0
 * @ClassName: NounCalibreRepostory
 * @date 2020/3/20 10:27 上午
 */
@Repository

public class NounCalibreRepostory {
    @Resource
    private MongoTemplate mongoTemplate;

    public NounCalibre save(NounCalibre nounCalibre) {
        return mongoTemplate.save(nounCalibre);
    }

    public UpdateResult update(NounCalibre nounCalibre) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(nounCalibre.getId()));
        Update update = new Update();
        update.set("operator",nounCalibre.getOperator());
        update.set("operateTime",nounCalibre.getOperateTime());
        update.set("content",nounCalibre.getContent());
        return mongoTemplate.upsert(query, update, NounCalibre.class);

    }

    public DeleteResult delete(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.remove(query, NounCalibre.class);
    }

    public NounCalibre queryOne() {
        return mongoTemplate.findOne(new Query(), NounCalibre.class);
    }

}
