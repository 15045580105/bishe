package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;

import java.util.List;
import java.util.Map;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 10:54 2020-04-01
 * @Modified By :
 */
public interface PotInformationService {

    void savePotInformation();

    List<Map<String, String>> selectBypage(String sortField,String sequence,String potName, long page, int count);

    TmpltAndPotStatistics PotStatistics(String startTime, String endTime);

    void savePotIp();
}
