package com.qianlima.reptile.statistics.service;


import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;


import java.util.Map;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 23:59 2020-03-18
 * @Modified By :
 */
public interface TemplateAndPotStatistical {

    void template();

    Map<String, TmpltAndPotStatistics> selectTemplate(String startTime,String endTime);
}
