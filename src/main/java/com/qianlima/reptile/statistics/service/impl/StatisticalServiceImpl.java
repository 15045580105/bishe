package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 22:50 2020-03-08
 * @Modified By :
 */

import com.qianlima.reptile.statistics.domain.FaultTmpltStatistics;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.OctopusMapper;
import com.qianlima.reptile.statistics.repository.TmpltStatisticsRepository;
import com.qianlima.reptile.statistics.service.StatisticalService;
import com.qianlima.reptile.statistics.utils.DateUtil;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gyx
 * @date 2020-03-08 22:50
 */
@Service
public class StatisticalServiceImpl implements StatisticalService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticalServiceImpl.class);
    @Autowired
    private ModmonitorMapper modmonitorMapper;
    @Autowired
    private OctopusMapper octopusMapper;
    @Autowired
    private TmpltStatisticsRepository tmpltStatisticsRepository;


    @Override
    public Map<String,List> statisticalData(String startTime,String endTime){
        Map<String,List> map = new HashMap<>();
        List<FaultTmpltStatistics> list1 = new ArrayList<>();
        List<FaultTmpltStatistics> list2 = new ArrayList<>();
        List<FaultTmpltStatistics> list3 = new ArrayList<>();
        List<FaultTmpltStatistics> list4 = new ArrayList<>();
        List<FaultTmpltStatistics> list5 = new ArrayList<>();
        List<FaultTmpltStatistics> list = tmpltStatisticsRepository.queryByTime(startTime,endTime);
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i).getType().equals("biddingFault")){
                list1.add(list.get(i));
            }else if(list.get(i).getType().equals("preProject")){
                list2.add(list.get(i));
            }else if(list.get(i).getType().equals("octopus")){
                list3.add(list.get(i));
            }else if(list.get(i).getType().equals("manual")){
                list4.add(list.get(i));
            }else{
                list5.add(list.get(i));
            }
        }
        map.put("biddingFault",list1);
        map.put("preProject",list2);
        map.put("octopus",list3);
        map.put("manual",list4);
        map.put("biddingMessyCode",list5);
        return map;
    }

    @Override
    public void statistical() {
        String reportStartTime = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
                //DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
        String startTime = reportStartTime + " 00:00:00";
        String endTime = reportStartTime + " 23:59:59";
        List<FaultTmpltStatistics> list0 = tmpltStatisticsRepository.queryByTime(startTime,endTime);
        if(list0.size()>0){
            logger.info("已经存在 {} 日的数据",reportStartTime);
        }else {
            List<Map<String, String>> list = new ArrayList<>();
            //招标故障模版器障模版人工处理和未处理
            List<FaultTmpltDo> list1 = modmonitorMapper.selectTenderProcessed(startTime, endTime);
            List<FaultTmpltDo> list2 = modmonitorMapper.selectTenderUntreated(startTime, endTime);
            String tenderUntreated = list2.size() + "";
            Map<String, String> map = conversionTender(list1, tenderUntreated);
            map.put("efficient", efficient(map));
            list.add(map);
            //前期故障模版服务器障
            List<Map<String, Object>> list3 = modmonitorMapper.selectEarlyServiceProcessed(startTime, endTime);
            String untreated = modmonitorMapper.selectEarlyServiceUntreated(startTime, endTime);
            Map<String, String> map0 = conversion(list3, "earlyService", untreated);
            list.add(map0);
            //前期故障模版解析故障
            List<Map<String, Object>> list4 = modmonitorMapper.selectEarlyParsingProcessed(startTime, endTime);
            String untreated1 = modmonitorMapper.selectEarlyParsingUntreated(startTime, endTime);
            Map<String, String> map1 = conversion(list4, "earlyParsing", untreated1);
            list.add(map1);
            //前期故障模版解析值故障
            List<Map<String, Object>> list5 = modmonitorMapper.selectEarlyValueProcessed(startTime, endTime);
            String untreated2 = modmonitorMapper.selectEarlyValueUntreated(startTime, endTime);
            Map<String, String> map2 = conversion(list5, "earlyValue", untreated2);
            list.add(map2);
            //乱码数据
            List<Map<String, Object>> list6 = modmonitorMapper.selectMessyCodeProcessed(DateUtil.date3TimeStamp(startTime), DateUtil.date3TimeStamp(endTime));
            String untreated3 = modmonitorMapper.selectMessyCodeUntreated(DateUtil.date3TimeStamp(startTime), DateUtil.date3TimeStamp(endTime));
            Map<String, String> map3 = conversion(list6, "biddingMessyCode", untreated3);
            map3.put("efficient", efficient(map3));
            list.add(map3);
            //八抓鱼
            List<Map<String, Object>> list7 = octopusMapper.selectStatusCodeProcessed(DateUtil.date2TimeStamp(startTime), DateUtil.date2TimeStamp(endTime));
            String untreated4 = octopusMapper.selectStatusCodeUntreated(DateUtil.date2TimeStamp(startTime), DateUtil.date2TimeStamp(endTime));
            Map<String, String> map4 = conversion(list7, "octopus", untreated4);
            map4.put("efficient", efficient(map4));
            list.add(map4);
            //人工编辑
            List<Map<String, Object>> list8 = modmonitorMapper.selectHumanEditorsProcessed(DateUtil.date2TimeStamp(startTime), DateUtil.date2TimeStamp(endTime));
            String untreated5 = modmonitorMapper.selectHumanEditorsUntreated(DateUtil.date2TimeStamp(startTime), DateUtil.date2TimeStamp(endTime));
            Map<String, String> map5 = conversion(list8, "manual", untreated5);
            map5.put("efficient", efficient(map5));
            list.add(map5);
            List<Map<String, String>> list9 = mergeMap(list);
            saveData(list9, startTime, endTime);
        }
    }


    /**
     * @return a
     * @description 前期项目数值合并
     * @author gyx
     * @date 2020-03-15 17:2
     * @parameter * @param null
     * @since
     */
    private List<Map<String, String>> mergeMap(List<Map<String, String>> list) {
        List<Map<String, String>> list1 = new ArrayList<>();
        Map<String, String> map = list.get(1);
        Map<String, String> map1 = list.get(2);
        Map<String, String> map2 = list.get(3);
        ;
        Map<String, String> map3 = new HashMap<>();
        for (int i = -1; i < 7; i++) {
            int a = 0, b = 0, c = 0;
            if (map.containsKey(i + "")) {
                a = Integer.parseInt(map.get(i + ""));
            }
            if (map1.containsKey(i + "")) {
                b = Integer.parseInt(map1.get(i + ""));
            }
            if (map2.containsKey(i + "")) {
                c = Integer.parseInt(map2.get(i + ""));
            }
            map3.put(i + "", (a + b + c) + "");
        }
        map3.put("total", (Integer.parseInt(map.get("total")) + Integer.parseInt(map1.get("total")) + Integer.parseInt(map1.get("total"))) + "");
        map3.put("totalName", "preProject");
        map3.put("efficient", efficient(map3));
        list1.add(list.get(0));
        list1.add(map3);
        list1.add(list.get(4));
        list1.add(list.get(5));
        list1.add(list.get(6));
        return list1;
    }

    /**
     * @return a
     * @description 计算有效率
     * @author gyx
     * @date 2020-03-15 17:1
     * @parameter * @param null
     * @since
     */
    private String efficient(Map<String, String> map) {
        double a = 0, b = 0, c = 0;
        if (StringUtils.isNotBlank(map.get(1 + ""))) {
            a = Integer.parseInt(map.get(1 + ""));
        }
        if (StringUtils.isNotBlank(map.get(3 + ""))) {
            b = Integer.parseInt(map.get(3 + ""));
        }
        if (StringUtils.isNotBlank(map.get(4 + ""))) {
            c = Integer.parseInt(map.get(4 + ""));
        }
        if (StringUtils.isNotBlank(map.get("-1")) && !map.get("-1").equals("0")) {
            double s = Integer.parseInt(map.get("-1"));
            double f = (a + b + c) / s;
            return String.format("%.4f", f);
        } else {
            return "";
        }
    }

    /**
     * @return a
     * @description 单表单各个状态数值统计
     * @author gyx
     * @date 2020-03-15 17:2
     * @parameter * @param null
     * @since
     */
    private Map<String, String> conversion(List<Map<String, Object>> list, String totalName, String untreated) {
        int a = 0;
        int y = 0;
        Map<String, String> map1 = new HashMap<>();
        //keySet获取map集合key的集合  然后在遍历key即可
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map2 = list.get(i);
            map1.put(map2.get("state").toString(), map2.get("count").toString());
        }
        for (Object o : map1.keySet()) {
            a = a + Integer.parseInt(map1.get(o));
        }
        if (StringUtils.isNotBlank(untreated)) {
            y = Integer.parseInt(untreated);
        }
        map1.put("0", y + "");
        map1.put("total", (a + y) + "");
        map1.put("-1", a + "");
        map1.put("totalName", totalName);
        return map1;
    }

    /**
     * @return a
     * @description 招标模版去重合并
     * @author gyx
     * @date 2020-03-15 17:2
     * @parameter * @param null
     * @since
     */
    private Map<String, String> conversionTender(List<FaultTmpltDo> list1, String tenderUntreated) {
        //建立已解决状态比较表
        Map<String, String> map3 = new HashMap<>();
        map3.put("1", "0");
        map3.put("3", "0");
        map3.put("4", "0");
        //各状态初始值
        int a = 0, b = 0, c = 0, d = 0, e = 0, f = 0;
        //最终数值map
        Map<String, Map<String, String>> map = new HashMap<>();
        //返回数值map
        Map<String, String> map1 = new HashMap<>();
        //Tmplt去重相同比状态，状态都是已解决比时间，因为查询用union所以完全相同数据已经被排除
        for (FaultTmpltDo faultTmpltDo : list1) {
            if (!map.containsKey(faultTmpltDo.getTmplt())) {
                Map<String, String> map2 = new HashMap<>();
                map2.put(faultTmpltDo.getState(), faultTmpltDo.getUpdateTime());
                map.put(faultTmpltDo.getTmplt(), map2);
            } else {
                String key = "";
                Map<String, String> map2 = map.get(faultTmpltDo.getTmplt());
                for (String o : map2.keySet()) {
                    key = o;
                }
                if (map3.containsKey(key) && map3.containsKey(faultTmpltDo.getState())) {
                    if (DateUtils.compareDate(map2.get(key), faultTmpltDo.getUpdateTime()) == 1) {
                        Map<String, String> map4 = new HashMap<>();
                        map4.put(faultTmpltDo.getState(), faultTmpltDo.getUpdateTime());
                        map.put(faultTmpltDo.getTmplt(), map4);
                    }
                }
                if (!map3.containsKey(key) && map3.containsKey(faultTmpltDo.getState())) {
                    Map<String, String> map4 = new HashMap<>();
                    map4.put(faultTmpltDo.getState(), faultTmpltDo.getUpdateTime());
                    map.put(faultTmpltDo.getTmplt(), map4);
                }
                if (!map3.containsKey(key) && !map3.containsKey(faultTmpltDo.getState())) {
                    if (DateUtils.compareDate(map2.get(key), faultTmpltDo.getUpdateTime()) == 1) {
                        Map<String, String> map4 = new HashMap<>();
                        map4.put(faultTmpltDo.getState(), faultTmpltDo.getUpdateTime());
                        map.put(faultTmpltDo.getTmplt(), map4);
                    }
                }
            }
        }
        //计算各个状态值
        for (String o : map.keySet()) {
            Map<String, String> map2 = map.get(o);
            for (String o1 : map2.keySet()) {
                if (o1.equals("1")) {
                    a++;
                } else if (o1.equals("2")) {
                    b++;
                } else if (o1.equals("3")) {
                    c++;
                } else if (o1.equals("4")) {
                    d++;
                } else if (o1.equals("5")) {
                    e++;
                } else {
                    f++;
                }
            }
        }
        //重组结果map
        map1.put("0", tenderUntreated);
        map1.put("1", a + "");
        map1.put("2", b + "");
        map1.put("3", c + "");
        map1.put("4", d + "");
        map1.put("5", e + "");
        map1.put("6", f + "");
        map1.put("total", (a + b + c + d + e + f + Integer.parseInt(tenderUntreated)) + "");
        map1.put("-1", (a + b + c + d + e + f) + "");
        map1.put("totalName", "biddingFault");
        return map1;
    }

    private void saveData(List<Map<String, String>> list, String startTime, String endTime) {
        String a = modmonitorMapper.selectPotUntreated(startTime, endTime);
        String b = modmonitorMapper.selectPotProcessed(startTime, endTime);
        String c = (Integer.parseInt(a) + Integer.parseInt(b)) + "";
        String time = DateUtil.getfromatString(startTime);
        for (int i = 0; i < list.size(); i++) {
            FaultTmpltStatistics faultTmpltStatistics = new FaultTmpltStatistics();
            Map<String, String> map = list.get(i);
            if (i == 0) {
                faultTmpltStatistics.setUnHandlePotAmount(Long.parseLong(a));
                faultTmpltStatistics.setHandledPotAmount(Long.parseLong(b));
                faultTmpltStatistics.setPotTotal(Long.parseLong(c));
            }
            faultTmpltStatistics.setType(map.get("totalName"));
            faultTmpltStatistics.setQueryDate(time);
            faultTmpltStatistics.setTmpltTotal(StringUtils.isNotBlank(map.get("total")) ? Long.parseLong(map.get("total")) : 0);
            faultTmpltStatistics.setUnHandleTmpltAmount(StringUtils.isNotBlank(map.get("0")) ? Long.parseLong(map.get("0")) : 0);
            faultTmpltStatistics.setHandledTmpltAmount(StringUtils.isNotBlank(map.get("-1")) ? Long.parseLong(map.get("-1")) : 0);
            faultTmpltStatistics.setNeedObserAmount(StringUtils.isNotBlank(map.get("2")) ? Long.parseLong(map.get("2")) : 0);
            faultTmpltStatistics.setNormalAmount(StringUtils.isNotBlank(map.get("6")) ? Long.parseLong(map.get("6")) : 0);
            faultTmpltStatistics.setModifyAmount(StringUtils.isNotBlank(map.get("1")) ? Long.parseLong(map.get("1")) : 0);
            faultTmpltStatistics.setAddAmount(StringUtils.isNotBlank(map.get("3")) ? Long.parseLong(map.get("3")) : 0);
            faultTmpltStatistics.setManulAmount(StringUtils.isNotBlank(map.get("4")) ? Long.parseLong(map.get("4")) : 0);
            faultTmpltStatistics.setInvalidAmount(StringUtils.isNotBlank(map.get("5")) ? Long.parseLong(map.get("5")) : 0);
            faultTmpltStatistics.setEfficient(StringUtils.isNotBlank(map.get("efficient")) ? Double.parseDouble(map.get("efficient")) : 0);
            tmpltStatisticsRepository.save(faultTmpltStatistics);
        }
    }

}

