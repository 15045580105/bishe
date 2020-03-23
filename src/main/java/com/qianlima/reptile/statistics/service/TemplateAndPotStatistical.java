package com.qianlima.reptile.statistics.service;


import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;
import com.qianlima.reptile.statistics.entity.Response;


import java.util.Map;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 23:59 2020-03-18
 * @Modified By :
 */
public interface TemplateAndPotStatistical {
    //定时查询模版和pot数
    void template();
    //查询统计结果
    Response selectTemplate(String startTime, String endTime);
}
