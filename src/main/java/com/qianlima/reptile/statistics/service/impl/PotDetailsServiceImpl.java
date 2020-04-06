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
    public Response getPotDetails(String potName, String states,String startTime) {
        PotDetailResponse potDetailResponse = new PotDetailResponse();
        PotDetail potDetail = getPotDetail(potName, states);
        potDetailResponse.setPotDetail(potDetail);
        potDetailResponse.setPotNotes(getPotNote(potDetail.getId()));
        potDetailResponse.setReleaseAndCollectCountMap(getTemplateCountInfo(potDetail.getDomain(),startTime));
        potDetailResponse.setTemplateInfos(getTemplateInfos(potDetail.getDomain()));
        potDetailResponse.setAssociatedPots(getAssociatedPot(potDetail.getDomain()));
        return Response.success(potDetailResponse);
    }


    private PotDetail getPotDetail(String potName, String states) {
        PotDetail potDetail = rawdatasMapper.selectPotDetailByPotName(potName);
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
            List<PotInformation> potInformations = potInformationRepository.queryByIp(potInformation.getRepeatPot(),potName);
            for (PotInformation information : potInformations) {
                associatedPot.add(information.getPot());
            }
        }
        return associatedPot;
    }

    private List<PotNote> getPotNote(Integer potId) {
        List<PotNote> potNotes = rawdatasMapper.selectPotNoteByPotId(potId);
        if (potNotes == null || potNotes.size() == 0) {
            return null;
        }
        return potNotes;
    }

    private List<TemplateInfo> getTemplateInfos(String potName) {
        List<TemplateInfo> list = rawdatasMapper.selectTemplateInfosByName(potName);
        return list;
    }

    private Map<String,ReleaseAndCollectCount> getTemplateCountInfo(String potName,String time) {
        List<String> ids = rawdatasMapper.selectTemplateIdByName(potName);
        Map<String, ReleaseAndCollectCount> map = new HashMap<>();
        Long startTime = DateUtils.str2TimeStamp(time, DateUtils.FUZSDF);
        Long endTime = startTime + 86399L;
        if (ids != null && ids.size() != 0) {
            for (int i = 0; i < 15; i++) {
                if (endTime > (System.currentTimeMillis() / 1000)) {
                    break;
                }
                ReleaseAndCollectCount releaseAndCollectCount = new ReleaseAndCollectCount();
                releaseAndCollectCount.setCollectCount(qianlimaMapper.selectBiddingRawByIds(ids, startTime, endTime));
                releaseAndCollectCount.setReleaseCount(qianlimaMapper.selectPhpcmsCountsByIds(ids, startTime, endTime));
                releaseAndCollectCount.setDate(DateUtils.getFormatDateStrBitAdd(endTime,DateUtils.FUZSDF));
                map.put(releaseAndCollectCount.getDate(), releaseAndCollectCount);
                startTime += 86400;
                endTime += 86400;
            }
//            for (Map.Entry<String, ReleaseAndCollectCount> stringReleaseAndCollectCountEntry : map.entrySet()) {
//                System.out.println(stringReleaseAndCollectCountEntry.getKey());
//                System.out.println(stringReleaseAndCollectCountEntry.getValue().toString());
//            }

        }
        return map;
    }
}
