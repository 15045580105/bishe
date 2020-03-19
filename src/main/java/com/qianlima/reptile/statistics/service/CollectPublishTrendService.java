package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

/**
 * @description: 采集量发布量趋势
 * @author: sx
 * @create: 2020-03-18 09:44:42
 **/
public interface CollectPublishTrendService {
    Response getYearTrend(String startTime, String endTime);

    Response getMonthTrend(String date);
}
