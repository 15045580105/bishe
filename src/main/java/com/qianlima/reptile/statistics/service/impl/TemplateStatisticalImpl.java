package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 18:49 2020-04-09
 * @Modified By :
 */

import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.RawdataMapper;
import com.qianlima.reptile.statistics.service.TemplateStatistical;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gyx
 * @date 2020-04-09 18:49
 */
@Service
public class TemplateStatisticalImpl implements TemplateStatistical {
    @Autowired
    private RawdataMapper rawdataMapper;
    @Autowired
    private ModmonitorMapper modmonitorMapper;

    @Override
    public Map<String,Object> templateStatistical(String startTime, String endTime){
        Map<String,Object> map0 = new HashMap<>();
        String startUsingNotReady = rawdataMapper.selectEnableTempltByTime(startTime,endTime)+"";
        List<String> startUsingNormal = rawdataMapper.selectEnableTempltTime(startTime,endTime);
        String notEnabled = rawdataMapper.selectToEnableTempltByTime(startTime,endTime)+"";
        String unclassified = rawdataMapper.selectUnclassifiedTempltByTime(startTime,endTime)+"";
        String delete = rawdataMapper.selectDeleteTempltByTime(startTime,endTime)+"";
        List<String> fial = modmonitorMapper.selectFailTempltId(startTime,endTime);
        List<String> failAll = fial;
        fial.retainAll(startUsingNormal);
        Map<String,String> map = new HashMap<>();
        map.put("startUsing",(Integer.parseInt(startUsingNotReady)+(startUsingNormal.size()-fial.size()))+"");
        map.put("notEnabled",notEnabled);
        map.put("unclassified",unclassified);
        map.put("delete",delete);
        map.put("fial",failAll.size()+"");
        List<String> list =  DateUtils.getDates(startTime, endTime);
        Collections.reverse(list);
        Map<String,Object> mapFail = new TreeMap<>();
        for (int i = 0; i <list.size()-2 ; i++) {
            Map<String,String> map1 = new HashMap<>();
            List<FaultTmpltDo> selectFailTemplt  = modmonitorMapper.selectFailTemplt((list.get(i) + DateUtils.dateStartStr),(list.get(i) + DateUtils.dateEndStr));
            List<FaultTmpltDo> selectFailTemplt1  = modmonitorMapper.selectFailTemplt((list.get(i+1) + DateUtils.dateStartStr),(list.get(i+1) + DateUtils.dateEndStr));
            long qiYong = Long.parseLong(map.get("startUsing"));
            selectFailTemplt.retainAll(selectFailTemplt1);
            map1.put("count",(selectFailTemplt1.size()-selectFailTemplt.size())+"");
            double s = (selectFailTemplt1.size()-selectFailTemplt.size())/qiYong;
            map1.put("rate",String.format("%.4f", s));
            mapFail.put(list.get(i+1),map1);
        }
        map0.put("bing",map);
        map0.put("shu",mapFail);
        return map0;
    }

}
