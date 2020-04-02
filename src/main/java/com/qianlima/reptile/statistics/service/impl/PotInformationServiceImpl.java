package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 11:00 2020-04-01
 * @Modified By :
 */

import com.alibaba.fastjson.JSON;
import com.qianlima.reptile.statistics.constant.TempStatus;
import com.qianlima.reptile.statistics.domain.PotInformation;
import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.entity.PhpcmsContentDo;
import com.qianlima.reptile.statistics.entity.PotDo;
import com.qianlima.reptile.statistics.entity.TempltDo;
import com.qianlima.reptile.statistics.job.InitPublishCountStatisticsJob;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.mapper.RawdataMapper;
import com.qianlima.reptile.statistics.repository.PotAndTmpltRepository;
import com.qianlima.reptile.statistics.repository.PotInformationRepository;
import com.qianlima.reptile.statistics.service.PotInformationService;
import com.qianlima.reptile.statistics.utils.DateUtil;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gyx
 * @date 2020-04-01 11:00
 */
@Service
public class PotInformationServiceImpl implements PotInformationService {
    private static final Logger logger = LoggerFactory.getLogger(InitPublishCountStatisticsJob.class);

    @Autowired
    private RawdataMapper rawdataMapper;
    @Autowired
    private ModmonitorMapper modmonitorMapper;
    @Autowired
    private PotInformationRepository potInformationRepository;
    @Autowired
    private QianlimaMapper qianlimaMapper;
    @Autowired
    private PotAndTmpltRepository potAndTmpltRepository;

    @Override
    public void selectPotInformation() {
        String today = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
        String reportStartTime = DateUtils.getLastDay(today);
        String monthEarly = DateUtils.monthEarly(reportStartTime);

        //查询所有模板
        List<TempltDo> templtList = rawdataMapper.selectTempt();
        Map<String, String> mapTmplt = convertToMap(templtList);
        // 查询昨日故障模板
        List<FaultTmpltDo> list = modmonitorMapper.selectFailTemplt(reportStartTime + DateUtils.dateStartStr, reportStartTime + DateUtils.dateEndStr);
        Map<String, String> abnormalTempMap = convertMap(list);
        // 查询所有pot
        List<PotDo> potList = rawdataMapper.selectPot();
        Map<String, String> mapInTime = convertMapIntime(potList);
        Map<String, String> mapUpTime = convertMapUptime(potList);
        HashMap<String, List<String>> potTempMap = new HashMap<>(100000);
        List<PhpcmsContentDo> list1 = qianlimaMapper.selectPhpcmsCounts(DateUtil.date4TimeStamp(monthEarly), DateUtil.date4TimeStamp(reportStartTime));
        List<PhpcmsContentDo> list2 = qianlimaMapper.selectBiddingRaw(DateUtil.date4TimeStamp(monthEarly), DateUtil.date4TimeStamp(reportStartTime));
        Map<String, String> mapCollect30 = convertToCollect30(list2);
        Map<String, String> mapReleas30 = convertToCollect30(list1);
        for (PotDo potDo : potList) {
            potTempMap.put(potDo.getPotName(), new ArrayList<>());
        }
        // 将pot 与模板的关联关系保存到 potTempMap 中
        for (TempltDo t : templtList) {
            if (potTempMap.containsKey(t.getPotName())) {
                potTempMap.get(t.getPotName()).add(t.getIds());
            }
        }
        //循环pot获取每个pot信息存储
        for (String potName : potTempMap.keySet()) {
            int s = 1;
            List<String> tempList = new ArrayList<>();
            long collect30 = 0;
            long releas30 = 0;
            String state = "";
            String ip = "";
            try {
                tempList = potTempMap.get(potName);
                for (int i = 0; i <tempList.size() ; i++) {
                    if(StringUtils.isNotBlank(mapCollect30.get(tempList.get(i)))){
                        collect30 = collect30 + Long.parseLong(mapCollect30.get(tempList.get(i)));
                    }
                    if(StringUtils.isNotBlank(mapReleas30.get(tempList.get(i)))){
                        releas30 = releas30 + Long.parseLong(mapReleas30.get(tempList.get(i)));
                    }
                }
                state = judgeSxtate(tempList, abnormalTempMap, mapTmplt);
                InetAddress ia2 = InetAddress.getByName(potName);
                if (StringUtils.isNotBlank(ia2.getHostAddress())) {
                    ip = ia2.getHostAddress();
                }
                save(reportStartTime, potName, tempList.size(), state, collect30, releas30, ip, mapInTime.get(potName), mapUpTime.get(potName));
                logger.info(" 插入pot为 {} 的第 {} 数据", potName,s);
                s++;
            } catch (UnknownHostException e) {
                save(reportStartTime, potName, tempList.size(), state, collect30, releas30, ip, mapInTime.get(potName), mapUpTime.get(potName));
                logger.info(" 插入pot为 {} 的第 {} 数据", potName,s);
                s++;
            }
        }


    }

    /**
     * @return a
     * @description 分页查询存在potName条件时变为准确查询
     * @author gyx
     * @date 2020-04-01 16:5
     * @parameter * @param null
     * @since
     */
    @Override
    public List<Map<String, String>> selectBypage(String potName, long page, int count) {
        long total = potInformationRepository.query();
        List<PotInformation> list = potInformationRepository.queryByPage(potName, page, count);
        List<Map<String, String>> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map1 = new HashMap<>();
            List<PotInformation> listRepeat = potInformationRepository.queryByIp(list.get(i).getRepeatPot());
            String json = JSON.toJSONString(list.get(i));
            map1.put("data", json);
            if (listRepeat.size() != 0) {
                map1.put("repeatPotCount", listRepeat.size() + "");
            } else {
                map1.put("repeatPotCount", 0 + "");
            }
            map1.put("total", total + "");
            map1.put("children", JSON.toJSONString(listRepeat));
            list1.add(map1);
        }
        return list1;
    }

    @Override
    public TmpltAndPotStatistics PotStatistics(String startTime, String endTime) {
        return potAndTmpltRepository.queryByTime(startTime, endTime);
    }

    public Map<String, String> convertToMap(List<TempltDo> templtDos) {
        return templtDos.stream().collect(Collectors.toMap(TempltDo::getIds, TempltDo::getState));
    }

    public Map<String, String> convertToCollect30(List<PhpcmsContentDo> list) {
        return list.stream().collect(Collectors.toMap(PhpcmsContentDo::getTmplt, PhpcmsContentDo::getCount));
    }

    public Map<String, String> convertToReleas30(List<PhpcmsContentDo> list) {
        return list.stream().collect(Collectors.toMap(PhpcmsContentDo::getTmplt, PhpcmsContentDo::getCount));
    }


    public Map<String, String> convertMap(List<FaultTmpltDo> templtDos) {
        return templtDos.stream().collect(Collectors.toMap(FaultTmpltDo::getTmplt, FaultTmpltDo::getTmplt));
    }

    public Map<String, String> convertMapIntime(List<PotDo> potDos) {
        return potDos.stream().collect(Collectors.toMap(PotDo::getPotName, PotDo::getInitTime));
    }

    public Map<String, String> convertMapUptime(List<PotDo> potDos) {
        return potDos.stream().collect(Collectors.toMap(PotDo::getPotName, PotDo::getUpdateTime));
    }

    /**
     * @return a
     * @description 判断pot状态的方法
     * @author gyx
     * @date 2020-04-01 16:5
     * @parameter * @param null
     * @since
     */
    private String judgeSxtate(List<String> tempList, Map<String, String> abnormalTempMap, Map<String, String> mapTmplt) {
        if (tempList.size() == 0) {
            // POT新建状态
            return "potNew";
        } else {
            boolean enable = false;
            int deleteNum = 0;
            for (String tempId : tempList) {
                if (TempStatus.enable.equals(mapTmplt.get(tempId))) {
                    enable = true;
                    break;
                }
                if (!TempStatus.toEnable.equals(mapTmplt.get(tempId))) {
                    deleteNum++;
                }
            }
            // 有一个模板是 启用，则这个pot就是启用
            if (enable) {
                return "potEnable";
            } else {
                // pot所有模板都是删除状态，则pot是废弃状态
                if (deleteNum == tempList.size()) {
                    return "potAbandoned";
                }
            }
        }
        // 判断pot异常量
        int abnormalNum = 0;
        for (String tempId : tempList) {
            if (abnormalTempMap != null && abnormalTempMap.containsKey(tempId)) {
                abnormalNum++;
            }
        }
        // 如果pot下的每一个模板都是异常状态，则pot是异常状态
        if (abnormalNum == tempList.size()) {
            return "potAbnormal";
        }
        return "";
    }


    /**
     * @return a
     * @description pot信息入库方法
     * @author gyx
     * @date 2020-04-01 16:5
     * @parameter * @param null
     * @since
     */
    private void save(String queryDate, String potName, long templtNum, String state, long collect30, long releas30, String ip, String inTime, String upTime) {
        PotInformation potInformation = new PotInformation();
        potInformation.setQueryDate(queryDate);
        potInformation.setPot(potName);
        potInformation.setTemplateNumber(templtNum);
        potInformation.setState(state);
        potInformation.setCollectNumber(collect30);
        potInformation.setReleaseNumber(releas30);
        potInformation.setCreateTime(inTime);
        potInformation.setUpdateTime(upTime);
        potInformation.setRepeatPot(ip);
        potInformationRepository.save(potInformation);
    }


}


