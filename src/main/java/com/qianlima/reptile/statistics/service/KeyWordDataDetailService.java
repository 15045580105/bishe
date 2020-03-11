package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.KeyWordDataDetailReq;
import com.qianlima.reptile.statistics.entity.Response;

/**
 * @description: 关键词统计详情
 * @author: sx
 * @create: 2020-03-11 11:03:27
 **/
public interface KeyWordDataDetailService {
    Response keyWordDataDetail(KeyWordDataDetailReq keyWordDataDetailReq);
}
