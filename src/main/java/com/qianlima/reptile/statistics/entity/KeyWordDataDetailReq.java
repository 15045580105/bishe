package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @description: 关键词统计详情
 * @author: sx
 * @create: 2020-03-11 11:05:38
 **/
@Data
public class KeyWordDataDetailReq {
    /**
     * 关键词
     */
    private String keyword;
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
