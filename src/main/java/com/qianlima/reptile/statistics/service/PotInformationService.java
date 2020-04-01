package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;

import java.util.Map;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 10:54 2020-04-01
 * @Modified By :
 */
public interface PotInformationService {

    void selectPotInformation();

    Map<String,Map<String, String>> selectBypage(String potName, long page, int count);

    TmpltAndPotStatistics PotStatistics(String startTime, String endTime);

}
