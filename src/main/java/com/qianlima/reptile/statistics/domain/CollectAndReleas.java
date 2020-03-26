package com.qianlima.reptile.statistics.domain;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 09:53 2020-03-20
 * @Modified By :
 */

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gyx
 * @date 2020-03-20 09:53
 */
@Document(collection = "collectAndReleas")
@Data
public class CollectAndReleas{
    @Id
    private ObjectId id;

    /**
     * 查询时间  格式 2020-02-06
     */
    private String queryDate;

    /**
     * 采集状态34
     */
    private long collect34;

    /**
     * 采集状态50
     */
    private long collect50;

    /**
     * 采集状态非34，50
     */
    private long collectResidue;
    /**
     * 采集总量
     */
    private long collect;

    /**
     * 发布总量
     */
    private long releas;

    /**
     * 发布人工
     */
    private long releasUser;

    /**
     * 系统发布
     */
    private long releasSystem;


    /**
     * 项目数据
     */
    private long releasProject;

    /**
     * 招标数据
     */
    private long releasTender;
}
