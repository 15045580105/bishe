package com.qianlima.reptile.statistics.service;

import java.util.Map;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 09:41 2020-03-20
 * @Modified By :
 */
public interface CollectAndReleaseService {

    void collectAndRelease();

    Map<String, String> collectAndReleaseCount(String startTime, String endTime);

}
