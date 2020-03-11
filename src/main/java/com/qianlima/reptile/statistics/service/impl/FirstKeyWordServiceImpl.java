package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.constant.StatisticsConstant;
import com.qianlima.reptile.statistics.entity.FirstKeyWordResult;
import com.qianlima.reptile.statistics.entity.KeyWordTask;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.mapper.SpiderPlatformMapper1;
import com.qianlima.reptile.statistics.mapper.RawDatasMapper1;
import com.qianlima.reptile.statistics.service.FirstKeyWordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 一级关键词统计实现类
 * @author: sx
 * @create: 2020-03-09 14:16:19
 **/
@Service
public class FirstKeyWordServiceImpl implements FirstKeyWordService {

    @Autowired
    private SpiderPlatformMapper1 spiderPlatformMapper1;
    @Autowired
    private RawDatasMapper1 rawDatasMapper1;

    @Override
    public Response firstKeyWordStatistics(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return Response.error(100,"开始时间或结束时间为空！");
        }
        try {
            startTime = new String(startTime.getBytes("iso-8859-1"), "gbk");
            endTime = new String(endTime.getBytes("iso-8859-1"), "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //序号
        int order = 1;
        List<Integer> ids = spiderPlatformMapper1.selectId();
        if(null == ids || ids.size() == 0) {
            return Response.error(101,"关键词id为空！");
        }
        //避免list扩容
        ArrayList<FirstKeyWordResult> resultList = new ArrayList<>(ids.size());
        for (Integer id: ids) {
            int countGuoXin = 0;
            int countZhaoBiao = 0;
            int countCaiZhao = 0;
            int countDaoHang = 0;
            int countQLM = 0;
            KeyWordTask keyWordTask = spiderPlatformMapper1.selectKeyWordsTaskById(id);
            if(null == keyWordTask) {
               continue;
            }
            String taskListUrls = keyWordTask.getTaskListUrls();
            for (String web : StatisticsConstant.WEBS) {
                Integer countData = rawDatasMapper1.countUrl(web, taskListUrls, startTime, endTime);
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
            resultList.add(new FirstKeyWordResult(order, taskListUrls, countGuoXin, countZhaoBiao, countCaiZhao, countDaoHang, countQLM, id, startTime ,endTime));
            order ++;

        }
        return Response.success(resultList);
    }
}
