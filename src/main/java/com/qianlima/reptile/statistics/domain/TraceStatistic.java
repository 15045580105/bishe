package com.qianlima.reptile.statistics.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: TraceStatistic
 * @date 2020/3/17 11:52 下午
 */
@Document(collection = "TraceStatistic")
@Data
public class TraceStatistic {

    @Id
    private ObjectId id;
    /**
     * 类型 0-发布量 1-采集量
     */
    private Integer type;

    /**
     * 主爬虫
     */
    private Integer mainCrawlerCount;

    /**
     * 八爪鱼
     */
    private Integer octopusCount;
    /**
     * 人工编辑
     */
    private Integer artificialEditCount;
    /**
     * 桥接页面
     */
    private Integer bridgePageCount;
    /**
     * 竞品
     */
    private Integer competeProductsCount;
    /**
     * 微信
     */
//    private Integer weChatCount;
    /**
     * 查询日期
     */
    private String queryDate;

    private Integer totalCount;

}
