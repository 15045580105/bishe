package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.domain.TraceStatistic;
import com.qianlima.reptile.statistics.entity.Response;

public interface TraceStatisticService {

    Response getTraceStatistic(String startTime, String endTime,Integer Type);

    void saveStatistic();

}
