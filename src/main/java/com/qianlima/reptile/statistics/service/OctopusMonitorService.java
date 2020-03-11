package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

public interface OctopusMonitorService {

    Response getOctopusStatistics(String startTime, String endTime);

}
