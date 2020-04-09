package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 18:20 2020-04-02
 * @Modified By :
 */

import com.qianlima.reptile.statistics.entity.ConfigedItLogDo;
import com.qianlima.reptile.statistics.entity.CrawlconfigDo;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.entity.NoteDo;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.mapper.RawdataMapper;
import com.qianlima.reptile.statistics.service.TemplateInformation;
import com.qianlima.reptile.statistics.utils.DateUtil;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gyx
 * @date 2020-04-02 18:20
 */
@Service
public class TemplateInformationImpl implements TemplateInformation {
    @Autowired
    private QianlimaMapper qianlimaMapper;
    @Autowired
    private RawdataMapper rawdataMapper;
    @Autowired
    private ModmonitorMapper modmonitorMapper;

    @Override
    public Map<String, List> templateInformation(String templt,String startTime) {
        String today = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
        String end = startTime;
        String reportStartTime = DateUtils.monthHelfEarly(startTime);
        List<String> list = new ArrayList<>();
        list = DateUtils.getDates(reportStartTime, end);
        Map<String, List> map = new HashMap<>();
        List<Map<String, String>> listData = new ArrayList<>();
        List<CrawlconfigDo> listCrawlconfig = new ArrayList<>();
        List<Map<String, String>> listCollectClass = new ArrayList<>();
        for (int i = 0; i < list.size()-1; i++) {
            Map<String, String> map2 = new TreeMap<>();
            String startTime1 = DateUtil.date3TimeStamp((list.get(i) + DateUtils.dateStartStr));
            String endTime = DateUtil.date3TimeStamp((list.get(i) + DateUtils.dateEndStr));
            String collect = qianlimaMapper.selectBiddingById(Long.parseLong(startTime1), Long.parseLong(endTime), templt).toString();
            String release = qianlimaMapper.selectPhpcmsById(Long.parseLong(startTime1), Long.parseLong(endTime), templt).toString();
            map2.put("time", list.get(i));
            map2.put("collect", collect);
            map2.put("release", release);
            listData.add(map2);
        }
        CrawlconfigDo crawlconfigDo = rawdataMapper.selectCrawConfigByid(templt);
        List<FaultTmpltDo> list1 = modmonitorMapper.selectFailTempltByTmplt(templt,today + DateUtils.dateStartStr, today + DateUtils.dateEndStr);
        if(list1.size() != 0){
            crawlconfigDo.setState("2");
        }
        String pageNumStr = "";
        try {
            Pattern pattern = Pattern.compile("PARA_CLASS(.*)\\n");
            Matcher m = pattern.matcher(crawlconfigDo.getConfigData());
            while (m.find()) {
                String s = m.group();
                System.out.println("s:" + s);
                pageNumStr = s.replace("PARA_CLASS", "").replace("=", "").replace("", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        crawlconfigDo.setConfigData(pageNumStr);
        listCrawlconfig.add(crawlconfigDo);
        Map<String, String> map1 = new HashMap<>();
        map1.put("user", crawlconfigDo.getUser());
        map1.put("updateTime", crawlconfigDo.getUpdateTime());
        map1.put("strategy", crawlconfigDo.getCollectStrategy());
        listCollectClass.add(map1);
        List<ConfigedItLogDo> listConfigedItLog = rawdataMapper.selectConfigeditLog(templt);
        List<NoteDo> listNote = rawdataMapper.selectNote(templt);
        map.put("data", listData);
        map.put("crawlconfig", listCrawlconfig);
        map.put("configedItLog", listConfigedItLog);
        map.put("note", listNote);
        map.put("collectClass", listCollectClass);
        return map;
    }
}
