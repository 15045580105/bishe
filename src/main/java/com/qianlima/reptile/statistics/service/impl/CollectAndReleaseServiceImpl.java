package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 09:46 2020-03-20
 * @Modified By :
 */

import com.qianlima.reptile.statistics.domain.CollectAndReleas;
import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.repository.CollectAndReleasRepository;
import com.qianlima.reptile.statistics.service.CollectAndReleaseService;
import com.qianlima.reptile.statistics.utils.DateUtil;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gyx
 * @date 2020-03-20 09:46
 */
@Service
public class CollectAndReleaseServiceImpl implements CollectAndReleaseService {
    @Autowired
    private QianlimaMapper qianlimaMapper;
    @Autowired
    private CollectAndReleasRepository collectAndReleasRepository;

    @Override
    public void collectAndRelease() {
        //取前一天
        String today = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
        String reportStartTime = DateUtils.getLastDay(today);
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

    @Override
    public Map<String, String> collectAndReleaseCount(String month) {
        List<CollectAndReleas> list = collectAndReleasRepository.queryByTime((month + "-01"), (month + "-31"));
        CollectAndReleas collectAndReleas = new CollectAndReleas();
        Map<String, String> map = new HashMap<>();
        //循环每天统计当月总量
        for (int i = 0; i < list.size(); i++) {
            collectAndReleas.setCollect(collectAndReleas.getCollect() + list.get(i).getCollect());
            collectAndReleas.setCollect34(collectAndReleas.getCollect34() + list.get(i).getCollect34());
            collectAndReleas.setCollect50(collectAndReleas.getCollect50() + list.get(i).getCollect50());
            collectAndReleas.setCollectResidue(collectAndReleas.getCollectResidue() + list.get(i).getCollectResidue());
            collectAndReleas.setReleas(collectAndReleas.getReleas() + list.get(i).getReleas());
            collectAndReleas.setReleasUser(collectAndReleas.getReleasUser() + list.get(i).getReleasUser());
            collectAndReleas.setReleasSystem(collectAndReleas.getReleasSystem() + list.get(i).getReleasSystem());
            collectAndReleas.setReleasProject(collectAndReleas.getReleasProject() + list.get(i).getReleasProject());
            collectAndReleas.setReleasTender(collectAndReleas.getReleasTender() + list.get(i).getReleasTender());
            collectAndReleas.setQueryDate(month);
        }
        map.put("queryDate", collectAndReleas.getQueryDate());
        map.put("collect", collectAndReleas.getCollect() + "");
        map.put("collect34", collectAndReleas.getCollect34() + "");
        map.put("collect50", collectAndReleas.getCollect50() + "");
        map.put("CollectResidue", collectAndReleas.getCollectResidue() + "");
        map.put("releas", collectAndReleas.getReleas() + "");
        map.put("releasProject", collectAndReleas.getReleasProject() + "");
        map.put("releasSystem", collectAndReleas.getReleasSystem() + "");
        map.put("releasTender", collectAndReleas.getReleasTender() + "");
        map.put("releasUser", collectAndReleas.getReleasUser() + "");
        map.put("efficientCollect34", efficient(collectAndReleas.getCollect(), collectAndReleas.getCollect34()));
        map.put("efficientCollect50", efficient(collectAndReleas.getCollect(), collectAndReleas.getCollect50()));
        map.put("efficientCollectResidue", efficient(collectAndReleas.getCollect(), collectAndReleas.getCollectResidue()));
        map.put("efficientReleas", efficient(collectAndReleas.getCollect(), collectAndReleas.getReleas()));
        map.put("efficientReleasUse", efficient(collectAndReleas.getReleas(), collectAndReleas.getReleasUser()));
        map.put("efficientReleasSystem", efficient(collectAndReleas.getReleas(), collectAndReleas.getReleasSystem()));
        map.put("efficientReleasProject", efficient(collectAndReleas.getReleas(), collectAndReleas.getReleasProject()));
        map.put("efficientReleasTender", efficient(collectAndReleas.getReleas(), collectAndReleas.getReleasTender()));
        return map;
    }

    /**
     * @return a
     * @description 计算比例
     * @author gyx
     * @date 2020-03-23 18:5
     * @parameter * @param null
     * @since
     */
    private String efficient(long total, long count) {
        double s = total;
        double c = count;
        double f = c / s;
        return String.format("%.4f", f);
    }
}
