package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.mapper.RawdatasMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-08 17:02
 **/
@Service
public class QianlimaStatisticsService {

    @Autowired
    private QianlimaMapper qianlimaMapper;

    @Autowired
    private RawdatasMapper rawdatasMapper;

    public Integer everyDayPublishCount(int startTime,int endTime){
        return qianlimaMapper.select(startTime,endTime);
    }

    public Integer everyDayCatchCount(int startTime,int endTime){
        return rawdatasMapper.select(startTime,endTime);
    }
}
