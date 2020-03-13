package com.qianlima.reptile.statistics.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Auther: mahao
 * @Date: 2020/3/13 15:41
 * @Description:
 */
@Document(collection = "faultTmpltStatistics")
@Data
public class FaultTmpltStatistics {

    @Id
    private ObjectId id;

    /**
     *  biddingFault、preProject、octopus、manual、biddingMessyCode
     */
    private String type;

    /**
     * 查询时间  格式 2020-02-06
     */
    private String queryDate;


    /**
     * 模板总数
     */
    private long tmpltTotal;

    /**
     * 未处理模板量
     */
    private long unHandleTmpltAmount;

    /**
     * 已处理模板量
     */
    private long handledTmpltAmount;

    /**
     * 未处理POT量
     */
    private long unHandlePotAmount;

    /**
     * 已处理POT量
     */
    private long handledPotAmount;

    /**
     * POT总数
     */
    private long potTotal;

    /**
     * 需观察
     */
    private long needObserAmount;

    /**
     * 正常
     */
    private long normalAmount;

    /**
     * 修改
     */
    private long modifyAmount;

    /**
     * 新增
     */
    private long addAmount;

    /**
     * 人工
     */
    private long manulAmount;

    /**
     * 无效
     */
    private long invalidAmount;
}
