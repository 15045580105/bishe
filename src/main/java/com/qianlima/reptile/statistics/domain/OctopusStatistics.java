package com.qianlima.reptile.statistics.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DataStatistics")
@Data
public class OctopusStatistics {

    @Id
    private ObjectId id;


    /**
     * 查询时间  格式 2020-02-06
     */
    private String queryDate;

    /**
     * 任务总量
     */
    private long total;

    /**
     * 白天任务执行量
     */
    private long dayTimeAmount;

    /**
     * 晚上任务执行量
     */
    private long nightTimeAmount;

    /**
     * 识别出日期未启动
     */
    private long recUnStartAmount;

    /**
     *  未识别量
     */
    private long unRecAmount;


    /**
     *  无法访问量
     */
    private long unAccessAmount;


}
