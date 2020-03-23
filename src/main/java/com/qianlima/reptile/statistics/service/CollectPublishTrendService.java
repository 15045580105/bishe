package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

/**
 * @description: 采集量发布量趋势
 * @author: sx
 * @create: 2020-03-18 09:44:42
 **/
public interface CollectPublishTrendService {
    /**
     * 集发布详情-年趋势
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return
     */
    Response getYearTrend(String startTime, String endTime);

    /**
     * 集发布详情-月趋势
     * @param date 当月日期
     * @return
     */
    Response getMonthTrend(String date);
}
