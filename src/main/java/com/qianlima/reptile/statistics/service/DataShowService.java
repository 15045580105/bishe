package com.qianlima.reptile.statistics.service;

import java.util.Map;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-11 13:53
 **/
public interface DataShowService {
    Map<String,Object> dataShow(Map<String,Object> requestParams);
    Map<String,Object> dataDisplay(Map<String,Object> requestParams);
}
