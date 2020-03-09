package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.constant.StatisticsConstant;
import com.qianlima.reptile.statistics.entity.FirstKeyWordResult;
import com.qianlima.reptile.statistics.entity.KeyWordTask;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.mapper.FirstKeyWordMapper;
import com.qianlima.reptile.statistics.service.FirstKeyWordStatistics;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 一级关键词统计实现类
 * @author: sx
 * @create: 2020-03-09 14:16:19
 **/
@Service
@Slf4j
public class FirstKeyWordStatisticsImpl implements FirstKeyWordStatistics {

    @Autowired
    private FirstKeyWordMapper firstKeyWordMapper;

    @Override
    public Response firstKeyWordStatistics(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return Response.error(100,"开始时间或结束时间为空！");
        }
        //序号
        int order = 1;
        List<Integer> ids = firstKeyWordMapper.selectId();
        if(null == ids || ids.size() == 0) {
            return Response.error(101,"关键词id为空！");
        }
        ArrayList<FirstKeyWordResult> resultList = new ArrayList<>(35);
        for (Integer id: ids) {
            int countGuoXin = 0;
            int countZhaoBiao = 0;
            int countCaiZhao = 0;
            int countDaoHang = 0;
            int countQLM = 0;
            KeyWordTask keyWordTask = firstKeyWordMapper.selectKeyWordsTaskById(id);
            if(null == keyWordTask) {
               continue;
            }
            String taskListUrls = keyWordTask.getTaskListUrls();
            for (String web : StatisticsConstant.WEBS) {
                Integer countData = firstKeyWordMapper.countUrl(web, taskListUrls, startTime, endTime);
                if ("chinabidding".equals(web)) {
                    countGuoXin = countGuoXin + countData;
                } else if ("zhaobiao".equals(web)) {
                    countZhaoBiao = countZhaoBiao + countData;
                } else if ("bidcenter".equals(web)) {
                    countCaiZhao = countCaiZhao + countData;
                } else if ("okcis".equals(web)) {
                    countDaoHang = countDaoHang + countData;
                } else {
                    countQLM = countQLM + countData;
                }
            }
            resultList.add(new FirstKeyWordResult(order, taskListUrls, countGuoXin, countZhaoBiao, countCaiZhao, countDaoHang, countQLM));
            order ++;

        }
        return Response.success(resultList);
    }
}
