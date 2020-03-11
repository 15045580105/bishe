package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.SecondKeyWordReq;
import com.qianlima.reptile.statistics.entity.SecondKeyWordResult;
import com.qianlima.reptile.statistics.mapper.SecondKeyWordMapper;
import com.qianlima.reptile.statistics.service.SecondKeyWordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @description: 二级关键词统计
 * @author: sx
 * @create: 2020-03-10 18:49:20
 **/
@Service
public class SecondKeyWordServiceImpl implements SecondKeyWordService {
    @Autowired
    private SecondKeyWordMapper secondKeyWordMapper;

    @Override
    public Response secondKeyWordStatistics(SecondKeyWordReq secondKeyWordReq) {
        if (null == secondKeyWordReq || secondKeyWordReq.getKeywordId() == null || StringUtils.isBlank(secondKeyWordReq.getStartTime()) || StringUtils.isBlank(secondKeyWordReq.getWeb()) || StringUtils.isBlank(secondKeyWordReq.getEndTime())) {
            return Response.error(100, "参数不能为空！");
        }
        String keywords = secondKeyWordMapper.selectKeywordsById(secondKeyWordReq.getKeywordId());
        if (StringUtils.isBlank(keywords)) {
            return Response.error(101, "二级关键词为空！");
        }
        String[] keywordArray = keywords.split(",");
        //避免list扩容
        ArrayList<SecondKeyWordResult> resultList = new ArrayList<>(keywordArray.length);
        int order = 1;
        for (String keyword : keywordArray) {
            Integer count = secondKeyWordMapper.countSecondKeyWord(secondKeyWordReq.getWeb(), keyword, secondKeyWordReq.getStartTime(), secondKeyWordReq.getEndTime());
            SecondKeyWordResult secondKeyWordResult = new SecondKeyWordResult(order, keyword, count, secondKeyWordReq.getStartTime(), secondKeyWordReq.getEndTime(), secondKeyWordReq.getWeb());
            resultList.add(secondKeyWordResult);
            order++;
        }
        return Response.success(resultList);
    }
}
