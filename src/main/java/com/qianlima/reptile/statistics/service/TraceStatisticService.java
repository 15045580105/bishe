package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.domain.TraceStatistic;
import com.qianlima.reptile.statistics.entity.Response;

public interface TraceStatisticService {

    /**
     * 发布量采集量数据统计
     * @param startTime
     * @param endTime
     * @return
     */
    Response getTraceStatistic(String startTime, String endTime);

    /**
     * 发布量采集量数据查询保存入mongo
     */
    void saveStatistic();

}
