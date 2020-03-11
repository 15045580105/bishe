package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.entity.KeyWordData;
import com.qianlima.reptile.statistics.entity.KeyWordDataDetailReq;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.mapper.RawDatasMapper1;
import com.qianlima.reptile.statistics.service.KeyWordDataDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @description: 关键词统计详情
 * @author: sx
 * @create: 2020-03-11 11:08:01
 **/
@Service
public class KeyWordDataDetailServiceImpl implements KeyWordDataDetailService {
    @Autowired
    private RawDatasMapper1 keyWordDataDetailMapper;
    
    @Override
    public Response keyWordDataDetail(KeyWordDataDetailReq keyWordDataDetailReq) {
        if (null == keyWordDataDetailReq || StringUtils.isBlank(keyWordDataDetailReq.getWeb()) || StringUtils.isBlank(keyWordDataDetailReq.getStartTime()) || StringUtils.isBlank(keyWordDataDetailReq.getKeyword()) || StringUtils.isBlank(keyWordDataDetailReq.getEndTime())) {
            return Response.error(100, "参数不能为空！");
        }
        try {
            keyWordDataDetailReq.setStartTime(new String(keyWordDataDetailReq.getStartTime().getBytes("iso-8859-1"), "gbk"));
            keyWordDataDetailReq.setEndTime(new String(keyWordDataDetailReq.getEndTime().getBytes("iso-8859-1"), "gbk"));
            keyWordDataDetailReq.setKeyword(new String(keyWordDataDetailReq.getKeyword().getBytes("iso-8859-1"), "gbk"));
            keyWordDataDetailReq.setWeb(new String(keyWordDataDetailReq.getWeb().getBytes("iso-8859-1"), "gbk"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<KeyWordData> keyWordDatas = keyWordDataDetailMapper.dataDetail(keyWordDataDetailReq);
        for (int i = 1; i <= keyWordDatas.size(); i++) {
            keyWordDatas.get(i -1).setOrder(i);
        }
        return Response.success(keyWordDatas);
    }
}
