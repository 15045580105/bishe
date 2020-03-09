package com.qianlima.reptile.statistics.service;

import java.util.List;
import java.util.Map;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 22:49 2020-03-08
 * @Modified By :
 */
public interface StatisticalService {

    List<Map<String, String>> statistical(String reportStartTime, String reportEndTime);
}
