package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.domain.OctopusStatistics;
import com.qianlima.reptile.statistics.entity.OctopusLogInfo;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.ResultCode;
import com.qianlima.reptile.statistics.mapper.OctopusMapper;
import com.qianlima.reptile.statistics.repository.OctopusStatisticsRepository;
import com.qianlima.reptile.statistics.service.OctopusMonitorService;
import com.qianlima.reptile.statistics.task.BazhuayuTask;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: OctopusMonitorServiceImpl
 * @date 2020/3/6 8:15 下午
 */
@Service
public class OctopusMonitorServiceImpl implements OctopusMonitorService {

    @Autowired
    private OctopusMapper octopusMapper;
    @Autowired
    private BazhuayuTask bazhuayuTask;

    @Autowired
    private OctopusStatisticsRepository octopusStatisticsRepository;

    @Override
    public Response getOctopusStatistics(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return Response.error(ResultCode.PARAM_NOT_BLANK.getCode(), ResultCode.PARAM_NOT_BLANK.getMsg());
        }
        if (startTime.equals(endTime)) {
            return Response.success(octopusStatisticsRepository.queryByTime(startTime));
        } else {
            return Response.success(octopusStatisticsRepository.queryInTime(startTime, endTime));
        }
    }

    public void saveCurrentDayStatisticsInfo() {
        octopusStatisticsRepository.save(getOctopusStatistics());
    }

    private OctopusStatistics getOctopusStatistics() {
        OctopusStatistics octopus = new OctopusStatistics();
        octopus.setQueryDate(DateUtils.getFormatDateStr(System.currentTimeMillis(), DateUtils.FUZSDF));
        if (BazhuayuTask.token.equals("")) {
            bazhuayuTask.getToken();
        }
        Map<Integer, String> taskGroupMap = bazhuayuTask.getAllTaskGroup();
        //获取所有任务
        List<Map<String, String>> taskList = bazhuayuTask.getTasks(taskGroupMap);
        octopus.setTotal(taskList.size());
        //获取当天时间 start：18：00：00 end：23：59：59
        long nowTime = System.currentTimeMillis();
        long todayStartTime = nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) + 18 * 3600 * 1000L;
        long todayEndTime = todayStartTime + 21599000L;
        List<OctopusLogInfo> logInfos = octopusMapper.getLogInfos(todayStartTime, todayEndTime);
        //填充数据
        for (OctopusLogInfo logInfo : logInfos) {
            if (logInfo.getStatus().equals("0")) {
                octopus.setRecUnStartAmount(logInfo.getCount());
            }
            if (logInfo.getStatus().equals("1")) {
                octopus.setDayTimeAmount(logInfo.getCount());
            }
            if (logInfo.getStatus().equals("4")) {
                octopus.setUnRecAmount(logInfo.getCount());
            }
        }
        // 晚上执行量
        long nightTimeAmount = octopus.getTotal() - octopus.getRecUnStartAmount() - octopus.getDayTimeAmount();
        octopus.setNightTimeAmount(nightTimeAmount);
        // 无法访问量
        long unAccessAmount = octopus.getTotal() - octopus.getRecUnStartAmount() - octopus.getDayTimeAmount() - octopus.getUnRecAmount();
        octopus.setUnAccessAmount(unAccessAmount);
        return octopus;
    }
}