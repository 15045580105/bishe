package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

/**
 * @description: 一级关键词统计
 * @author: sx
 * @create: 2020-03-09 14:13:12
 **/
public interface FirstKeyWordService {
    Response firstKeyWordStatistics(String startTime, String endTime);
}
