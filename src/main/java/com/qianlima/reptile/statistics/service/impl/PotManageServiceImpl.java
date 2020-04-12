package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.entity.PotManageTmpInfo;
import com.qianlima.reptile.statistics.entity.PotManageTmplReq;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.service.PotManageService;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: PotManageServiceImpl
 * @date 2020/4/10 11:06 上午
 */
@Service
public class PotManageServiceImpl implements PotManageService {
    @Autowired
    private ModmonitorMapper modmonitorMapper;
    @Autowired
    private QianlimaMapper qianlimaMapper;

    @Override
    public Response getPotMangeTmplInfos(PotManageTmplReq potManageTmplReq) {
        Long endTimestamp = DateUtils.str2TimeStamp(DateUtils.getTodayEndTime(), DateUtils.FUZSDF);
        Long startTimestamp = endTimestamp - 86400 * 30;
        if (potManageTmplReq.getPage() == null) {
            potManageTmplReq.setPage(0);
        }
        if (potManageTmplReq.getSize() == null) {
            potManageTmplReq.setSize(10);
        }
        List<PotManageTmpInfo> potManageTmpInfos = modmonitorMapper
                .selectTemplateInfosByMultConditions(
                        potManageTmplReq.getId(),
                        potManageTmplReq.getState(),
                        potManageTmplReq.getCat(),
                        potManageTmplReq.getSortField(),
                        potManageTmplReq.getSortMode(),
                        potManageTmplReq.getPage() * potManageTmplReq.getSize(),
                        potManageTmplReq.getSize());

        Integer total = modmonitorMapper.selectTotalCountByMultConditions(
                potManageTmplReq.getId(),
                potManageTmplReq.getState(),
                potManageTmplReq.getCat());

        for (PotManageTmpInfo potManageTmpInfo : potManageTmpInfos) {
            potManageTmpInfo.setMonthReleaseCount(
                    qianlimaMapper.selectPhpcmsById(
                            startTimestamp, endTimestamp, String.valueOf(potManageTmpInfo.getId())));
            potManageTmpInfo.setMonthCollectCount(
                    qianlimaMapper.selectBiddingById(
                            startTimestamp, endTimestamp, String.valueOf(potManageTmpInfo.getId())));
            potManageTmpInfo.setTotal(total);
            potManageTmpInfo.setModifyTimes(modmonitorMapper.selectModifyTimesCountById(potManageTmpInfo.getId()));
            potManageTmpInfo.setCollectStrategyChangeTimes(modmonitorMapper.selectCollectStrategyChangeTimesById(potManageTmpInfo.getId()));
        }
        return Response.success(potManageTmpInfos);
    }




}
