package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @description: 二级关键词统计请求参数
 * @author: sx
 * @create: 2020-03-10 18:41:22
 **/
@Data
public class SecondKeyWordReq {
    /**
     * 关键词id
     */
    private Integer keywordId;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 网站
     */
    private String web;
}
