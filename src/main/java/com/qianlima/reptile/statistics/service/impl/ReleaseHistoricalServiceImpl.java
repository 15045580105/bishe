package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 23:23 2020-03-24
 * @Modified By :
 */

import com.qianlima.reptile.statistics.domain.CollectAndReleas;
import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.repository.CollectAndReleasRepository;
import com.qianlima.reptile.statistics.service.ReleaseHistoricalService;
import com.qianlima.reptile.statistics.utils.DateUtil;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyx
 * @date 2020-03-24 23:23
 */
@Service
public class ReleaseHistoricalServiceImpl implements ReleaseHistoricalService {
    @Autowired
    private QianlimaMapper qianlimaMapper;
    @Autowired
    private CollectAndReleasRepository collectAndReleasRepository;



    @Override
    public void historical(String start,String end) {
        List<String> list = DateUtils.getDates(start, end);
        for (int i = 0; i < list.size(); i++) {
            String reportStartTime = list.get(i);
            String startTime = DateUtil.date3TimeStamp((reportStartTime + DateUtils.dateStartStr));
            String endTime = DateUtil.date3TimeStamp((reportStartTime + DateUtils.dateEndStr));
            CollectAndReleas collectAndReleas = new CollectAndReleas();
            //查询采集总量
            long collect = qianlimaMapper.selectCollectCount(startTime, endTime);
            //查询采集34
            long collect34 = qianlimaMapper.selectCollect34(startTime, endTime);
            //查询采集50
            long collect50 = qianlimaMapper.selectCollect50(startTime, endTime);
            //非3450
            long collectResidue = collect - collect34 - collect50;
            //查询发布总量
            long releas = qianlimaMapper.selecRelease(startTime, endTime);
            //人工发布
            long releasUser = qianlimaMapper.selectReleaseUser(startTime, endTime);
            //系统发布
            long releasSystem = releas - releasUser;
            //项目发布
            long releasProject = qianlimaMapper.selectReleaseProject(startTime, endTime);
            //招标发布
            long releasTender = releas - releasProject;
            collectAndReleas.setCollect(collect);
            collectAndReleas.setCollect34(collect34);
            collectAndReleas.setCollect50(collect50);
            collectAndReleas.setCollectResidue(collectResidue);
            collectAndReleas.setReleas(releas);
            collectAndReleas.setReleasUser(releasUser);
            collectAndReleas.setReleasSystem(releasSystem);
            collectAndReleas.setReleasProject(releasProject);
            collectAndReleas.setReleasTender(releasTender);
            collectAndReleas.setQueryDate(reportStartTime);
            collectAndReleasRepository.save(collectAndReleas);
        }
    }
}
