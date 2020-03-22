package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 00:00 2020-03-19
 * @Modified By :
 */


import com.qianlima.reptile.statistics.constant.TempStatus;
import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.entity.TempltDo;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.RawdataMapper;
import com.qianlima.reptile.statistics.repository.PotAndTmpltRepository;
import com.qianlima.reptile.statistics.service.TemplateAndPotStatistical;
import com.qianlima.reptile.statistics.utils.DateUtil;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author gyx
 * @date 2020-03-19 00:00
 */
@Service
public class TemplateAndPotStatisticalImpl implements TemplateAndPotStatistical {
    private static final Logger logger = LoggerFactory.getLogger(TemplateAndPotStatisticalImpl.class);


    @Autowired
    private RawdataMapper rawdataMapper;
    @Autowired
    private ModmonitorMapper modmonitorMapper;
    @Autowired
    private PotAndTmpltRepository potAndTmpltRepository;

    @Override
    public void template() {
        String reportStartTime = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
        //查询所有模板
        List<TempltDo> templtList = rawdataMapper.selectTemplt();
        Map<Integer, String> mapTmplt = convertToMap(templtList);
        // 查询今日故障模板
        List<FaultTmpltDo> list = modmonitorMapper.selectFailTemplt(reportStartTime + DateUtils.dateStartStr, reportStartTime + DateUtils.dateEndStr);
        Map<String, String> abnormalTempMap = convertMap(list);
        // 模板总量
        long templtTotal = templtList.size();
        int potEnable = 0, potAbandoned = 0, potNew = 0, potAbnormal = 0;
        // 查询所有pot
        List<String> potList = rawdataMapper.selectAllPot();
        // pot总量
        long potTotal = potList.size();
        HashMap<String, List<Integer>> potTempMap = new HashMap<>(100000);
        for (String pot : potList) {
            potTempMap.put(pot, new ArrayList<>());
        }
        // 将pot 与模板的关联关系保存到 potTempMap 中
        for (TempltDo t : templtList) {
            if (potTempMap.containsKey(t.getPotName())){
                potTempMap.get(t.getPotName()).add(t.getId());
            }
        }
        // 便利 pot 与 模板 的关联map，统计pot状态
        long start = System.currentTimeMillis();
        for (String potName : potTempMap.keySet()) {
            List<Integer> tempList = potTempMap.get(potName);
            if (tempList.size() == 0) {
                // POT新建状态
                potNew++;
            } else {
                boolean enable = false;
                int deleteNum = 0;
                for (Integer tempId:tempList) {
                    if (TempStatus.enable.equals(mapTmplt.get(tempId))) {
                        enable = true;
                        break;
                    }
                    if (!TempStatus.toEnable.equals(mapTmplt.get(tempId))) {
                        deleteNum ++;
                    }
                }
                // 有一个模板是 启用，则这个pot就是启用
                if (enable) {
                    potEnable++;
                } else {
                    // pot所有模板都是删除状态，则pot是废弃状态
                    if(deleteNum == tempList.size() ){
                        potAbandoned++;
                    }
                }
            }
            // 判断pot异常量
            int abnormalNum = 0;
            for (Integer tempId : tempList) {
                if (abnormalTempMap !=null && abnormalTempMap.containsKey(tempId)) {
                    abnormalNum ++;
                }
            }
            // 如果pot下的每一个模板都是异常状态，则pot是异常状态
            if (abnormalNum == tempList.size()) {
                potAbnormal++;
            }
        }
        // 保存统计结果
        save(reportStartTime,templtTotal,potTotal,potAbandoned,potAbnormal,potEnable,potNew);
        logger.info("selectTmpltByPot use time = {}",System.currentTimeMillis() - start);
    }

    private void save(String reportStartTime,long templtTotal,long potTotal,long potAbandoned,long potAbnormal,long potEnable ,long potNew){
        TmpltAndPotStatistics tmpltAndPotStatistics = new TmpltAndPotStatistics();
        long endOfTheMonth = 0;
        if (DateUtils.isLastDayOfMonth(DateUtils.parseDateFromDateStr(reportStartTime))) {
            endOfTheMonth = 1;
        }
        tmpltAndPotStatistics.setEndOfTheMonth(endOfTheMonth);
        // 模板启用量
        long enableTemplt = rawdataMapper.selectEnableTemplt();
        // 待启用模板量
        long toEnableTemplt = rawdataMapper.selectToEnableTemplt();
        // 模板未分类量
        long unclassifiedTemplt = rawdataMapper.selectUnclassifiedTemplt();
        // 故障模板量
        long failTemplt = modmonitorMapper.selectFailTempltCount(reportStartTime + DateUtils.dateStartStr, reportStartTime + DateUtils.dateEndStr);
        tmpltAndPotStatistics.setTemplateUsing(enableTemplt);
        tmpltAndPotStatistics.setTemplatToEnable(toEnableTemplt);
        tmpltAndPotStatistics.setTemplateAbnormal(failTemplt);
        tmpltAndPotStatistics.setTemplateunClassified(unclassifiedTemplt);
        tmpltAndPotStatistics.setTemplateDelete(templtTotal - enableTemplt - toEnableTemplt );
        tmpltAndPotStatistics.setPotTotal(potTotal);
        tmpltAndPotStatistics.setTemplateTotal(templtTotal);
        tmpltAndPotStatistics.setPotAbandoned(potAbandoned);
        tmpltAndPotStatistics.setPotAbnormal(potAbnormal);
        tmpltAndPotStatistics.setPotNew(potNew);
        tmpltAndPotStatistics.setPotUsing(potEnable);
        tmpltAndPotStatistics.setQueryDate(reportStartTime);
        potAndTmpltRepository.save(tmpltAndPotStatistics);
    }

    public Map<String, String> convertMap(List<FaultTmpltDo> templtDos) {
        return templtDos.stream().collect(Collectors.toMap(FaultTmpltDo::getTmplt, FaultTmpltDo::getTmplt));
    }

    public Map<Integer, String> convertToMap(List<TempltDo> templtDos) {
        return templtDos.stream().collect(Collectors.toMap(TempltDo::getId, TempltDo::getState));
    }

    private Map<String, String> buildMap(List<FaultTmpltDo> list) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i).getTmplt(), list.get(i).getState());
        }
        return map;
    }

    @Override
    public Map<String, TmpltAndPotStatistics> selectTemplate(String startTime, String endTime) {
        String mon = null;
        Map<String, TmpltAndPotStatistics> map = new HashMap<>();
        String reportStartTime = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
        if (!DateUtils.getLocalDate(startTime).equals(DateUtils.getLocalDate(reportStartTime))) {
            if (DateUtils.getLocalDate(endTime).equals(DateUtils.getLocalDate(reportStartTime))) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, -1);
                Date m = c.getTime();
                mon = format.format(m);
            } else {
                mon = endTime;
            }
            List<TmpltAndPotStatistics> list = potAndTmpltRepository.queryByTimeAndMonth(startTime, mon);
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).getQueryDate(), list.get(i));
            }
        }
        List<TmpltAndPotStatistics> list1 = potAndTmpltRepository.queryByTime((startTime + "01"), (endTime + "31"));
        map.put(list1.get(0).getQueryDate(), list1.get(0));
        return map;
    }

}
