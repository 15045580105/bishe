package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.domain.PotInformation;
import com.qianlima.reptile.statistics.entity.*;
import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.mapper.RawdatasMapper;
import com.qianlima.reptile.statistics.repository.PotInformationRepository;
import com.qianlima.reptile.statistics.service.PotDetailsService;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: PotDetailsServiceImpl
 * @date 2020/4/1 2:55 下午
 */
@Service
public class PotDetailsServiceImpl implements PotDetailsService {
    @Autowired
    private RawdatasMapper rawdatasMapper;
    @Autowired
    private QianlimaMapper qianlimaMapper;
    @Autowired
    private PotInformationRepository potInformationRepository;

    @Override
    public Response getPotDetails(Integer potId, String states) {
        PotDetailResponse potDetailResponse = new PotDetailResponse();
        PotDetail potDetail = getPotDetail(potId, states);
        potDetailResponse.setPotDetail(potDetail);
        potDetailResponse.setPotNote(getPotNote(potId));
        potDetailResponse.setReleaseAndCollectCountMap(getTemplateCountInfo(potDetail.getName()));
        potDetailResponse.setTemplateInfos(getTemplateInfos(potDetail.getName()));
        potDetailResponse.setAssociatedPots(getAssociatedPot(potDetail.getName()));

        return Response.success(potDetailResponse);
    }


    private PotDetail getPotDetail(Integer potId, String states) {
        PotDetail potDetail = rawdatasMapper.selectPotDetailById(potId);
        potDetail.setStates(states);
        return potDetail;
    }

    private List<String> getAssociatedPot(String potName) {
        List<String> associatedPot = new ArrayList<>();
        PotInformation potInformation = potInformationRepository.queryByName(potName);
        if (potInformation != null) {
            if (StringUtils.isBlank(potInformation.getRepeatPot())) {
                return null;
            }
            List<PotInformation> potInformations = potInformationRepository.queryByIp(potInformation.getRepeatPot());
            for (PotInformation information : potInformations) {
                associatedPot.add(information.getRepeatPot());
            }
        }
        return associatedPot;
    }

    private PotNote getPotNote(Integer potId) {
        PotNote potNote = rawdatasMapper.selectPotNoteByPotId(potId);
        return potNote;
    }

    private List<TemplateInfo> getTemplateInfos(String potName) {
        List<TemplateInfo> list = rawdatasMapper.selectTemplateInfosByName(potName);
        return list;
    }

    private Map<String,ReleaseAndCollectCount> getTemplateCountInfo(String potName) {
        List<String> ids = rawdatasMapper.selectTemplateIdByName(potName);
        Map<String, ReleaseAndCollectCount> map = new HashMap<>();
        Long startTime = DateUtils.getYesterTodayStartTime();
        Long endTime = DateUtils.getYesterTodayEndTime();
        if (ids != null && ids.size() != 0) {
            for (int i = 0; i < 15; i++) {
                ReleaseAndCollectCount releaseAndCollectCount = new ReleaseAndCollectCount();
                releaseAndCollectCount.setCollectCount(qianlimaMapper.selectBiddingRawByIds(ids, startTime, endTime));
                releaseAndCollectCount.setReleaseCount(qianlimaMapper.selectPhpcmsCountsByIds(ids, startTime, endTime));
                releaseAndCollectCount.setDate(DateUtils.getFormatDateStrBitAdd(endTime,DateUtils.FUZSDF));
                map.put(releaseAndCollectCount.getDate(), releaseAndCollectCount);
                startTime -= 86400;
                endTime -= 86400;
            }
//            for (Map.Entry<String, ReleaseAndCollectCount> stringReleaseAndCollectCountEntry : map.entrySet()) {
//                System.out.println(stringReleaseAndCollectCountEntry.getKey());
//                System.out.println(stringReleaseAndCollectCountEntry.getValue().toString());
//            }

        }
        return map;
    }
}
