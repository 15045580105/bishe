package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 22:50 2020-03-08
 * @Modified By :
 */

import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.OctopusMapper;
import com.qianlima.reptile.statistics.service.StatisticalService;
import com.qianlima.reptile.statistics.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gyx
 * @date 2020-03-08 22:50
 */
@Service
public class StatisticalServiceImpl implements StatisticalService {
    @Autowired
    private ModmonitorMapper modmonitorMapper;
    @Autowired
    private OctopusMapper octopusMapper;

    @Override
    public List<Map<String, String>> statistical(String reportStartTime, String reportEndTime){
        if (StringUtils.isBlank(reportStartTime) || StringUtils.isBlank(reportEndTime)) {
            reportStartTime = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
            reportEndTime = reportStartTime;
        }
        reportStartTime = reportStartTime + " 00:00:00";
        reportEndTime = reportEndTime + " 23:59:59";
        List<Map<String, String>> list = new ArrayList<>();
        List<Map<String, Object>> list1 = modmonitorMapper.selectService(reportStartTime,reportEndTime);
        Map<String, String> map = conversion(list1, "tenderService");
        list.add(map);
        List<Map<String, Object>> list2 = modmonitorMapper.selectParsing(reportStartTime,reportEndTime);
        Map<String, String> map1 = conversion(list2, "tenderParsing");
        list.add(map1);
        List<Map<String, Object>> list3 = modmonitorMapper.selectValue(reportStartTime,reportEndTime);
        Map<String, String> map2 = conversion(list3, "tenderValue");
        list.add(map2);
        List<Map<String, Object>> list4 = modmonitorMapper.selectEarlyService(reportStartTime,reportEndTime);
        Map<String, String> map3 = conversion(list4, "earlyService");
        list.add(map3);
        List<Map<String, Object>> list5 = modmonitorMapper.selectEarlyParsing(reportStartTime,reportEndTime);
        Map<String, String> map4 = conversion(list5, "earlyParsing");
        list.add(map4);
        List<Map<String, Object>> list6 = modmonitorMapper.selectEarlyValue(reportStartTime,reportEndTime);
        Map<String, String> map5 = conversion(list6, "earlyValue");
        list.add(map5);
        List<Map<String, Object>> list7 = modmonitorMapper.selectMessyCode(DateUtil.date2TimeStamp(reportStartTime),DateUtil.date2TimeStamp(reportEndTime));
        Map<String, String> map6 = conversion(list7, "MessyCode");
        map6.put("efficient",efficient(map6));
        list.add(map6);
        List<Map<String, Object>> list8 = octopusMapper.selectStatusCode(reportStartTime,reportEndTime);
        Map<String, String> map7 = conversion(list8, "statusCode");
        map7.put("efficient",efficient(map7));
        list.add(map7);
        List<Map<String, Object>> list9 = modmonitorMapper.selectHumanEditors(reportStartTime,reportEndTime);
        Map<String, String> map8 = conversion(list9, "humanEditors");
        map8.put("efficient",efficient(map8));
        list.add(map8);
        return mergeMap(list,reportStartTime,reportEndTime);
    }


    private List<Map<String, String>> mergeMap(List<Map<String, String>> list,String startTime, String endTime){
        List<Map<String, String>> list1 = new ArrayList<>();
        Map<String, String> map = list.get(0);
        Map<String, String> map1 = list.get(1);
        Map<String, String> map2 = list.get(2);
        Map<String, String> map3 = list.get(3);
        Map<String, String> map4 = list.get(4);
        Map<String, String> map5 = list.get(5);
        Map<String, String> map6 = new HashMap<>();
        Map<String, String> map7 = new HashMap<>();
        for (int i = -2; i <7 ; i++) {
            int a = 0,b = 0,c = 0;
            int d = 0,e = 0,f = 0;
            if(map.containsKey(i)){a = Integer.parseInt(map.get(i));}
            if(map1.containsKey(i)){b = Integer.parseInt(map1.get(i));}
            if(map2.containsKey(i)){c = Integer.parseInt(map2.get(i));}
            if(map3.containsKey(i)){d = Integer.parseInt(map3.get(i));}
            if(map4.containsKey(i)){e = Integer.parseInt(map4.get(i));}
            if(map5.containsKey(i)){f = Integer.parseInt(map5.get(i));}
            map6.put(i+"",(a+b+c)+"");
            map7.put(i+"",(d+e+f)+"");
        }
        map6.put("total",(Integer.parseInt(map.get("total"))+Integer.parseInt(map1.get("total"))+Integer.parseInt(map1.get("total")))+"");
        map7.put("total",(Integer.parseInt(map1.get("total"))+Integer.parseInt(map1.get("total"))+Integer.parseInt(map1.get("total")))+"");
        map6.put("potTotal",modmonitorMapper.selectPotTotal(startTime,endTime));
        map6.put("potUntreated",modmonitorMapper.selectPotUntreated(startTime,endTime));
        map6.put("potProcessed",modmonitorMapper.selectPotProcessed(startTime,endTime));
        map6.put("totalName","tender");
        map7.put("totalName","early");
        map6.put("efficient",efficient(map6));
        map7.put("efficient",efficient(map7));
        list1.add(map6);
        list1.add(map7);
        list1.add(list.get(6));
        list1.add(list.get(7));
        list1.add(list.get(8));
        return list1;
    }

    private String efficient(Map<String, String> map){
        int a=0,b=0,c=0;
        if(StringUtils.isNotBlank(map.get("1"))){a = Integer.parseInt(map.get("1"));}
        if(StringUtils.isNotBlank(map.get("3"))){a = Integer.parseInt(map.get("3"));}
        if(StringUtils.isNotBlank(map.get("4"))){a = Integer.parseInt(map.get("4"));}
        if(StringUtils.isNotBlank(map.get("total")) && !map.get("total").equals("0")){
            double f = (a+b+c)/Integer.parseInt(map.get("total"));
            return String.format("%.2f", f);
        }else{
            return "";
        }
    }

    private Map<String, String> conversion(List<Map<String, Object>> list,String totalName) {
        int a = 0;
        int y;
        Map<String, String> map1 = new HashMap<>();
        //keySet获取map集合key的集合  然后在遍历key即可
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map2 = list.get(i);
            map1.put(map2.get("state").toString(), map2.get("count").toString());
        }
        for (Object o : map1.keySet()) {
            a = a + Integer.parseInt(map1.get(o));
        }
        y = a;
        if (map1.containsKey("0")) {
            y = y - Integer.parseInt(map1.get("0"));
        }
        if (map1.containsKey("-2")) {
            y = y - Integer.parseInt(map1.get("-2"));
        }
        map1.put("total",a+"");
        map1.put("-1",y+"");
        map1.put("totalName",totalName);
        return map1;
    }
}
