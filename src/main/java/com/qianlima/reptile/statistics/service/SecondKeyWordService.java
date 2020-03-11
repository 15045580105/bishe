package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.SecondKeyWordReq;

/**
 * @description: 二级关键词统计服务
 * @author: sx
 * @create: 2020-03-10 18:46:32
 **/
public interface SecondKeyWordService {
    Response secondKeyWordStatistics(SecondKeyWordReq secondKeyWordReq);
}
