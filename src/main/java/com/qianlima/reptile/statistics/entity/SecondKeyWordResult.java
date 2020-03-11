package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @description: 二级关键词统计结果
 * @author: sx
 * @create: 2020-03-11 10:27:11
 **/
@Data
public class SecondKeyWordResult {
    /**
     * 序号
     */
    private Integer order;
    /**
     * 二级关键词
     */
    private String keyWord;
    /**
     * 二级关键词统计数
     */
    private Integer count;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 结束时间
     */
    private String web;

    public SecondKeyWordResult(Integer order, String keyWord, Integer count, String startTime, String endTime, String web) {
        this.order = order;
        this.keyWord = keyWord;
        this.count = count;
        this.startTime = startTime;
        this.endTime = endTime;
        this.web = web;
    }
}
