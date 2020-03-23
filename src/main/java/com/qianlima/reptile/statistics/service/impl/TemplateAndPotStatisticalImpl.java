package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 00:00 2020-03-19
 * @Modified By :
 */


import com.qianlima.reptile.statistics.constant.ResultCode;
import com.qianlima.reptile.statistics.constant.TempStatus;
import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.TempltDo;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.RawdataMapper;
import com.qianlima.reptile.statistics.repository.PotAndTmpltRepository;
import com.qianlima.reptile.statistics.service.BaseService;
import com.qianlima.reptile.statistics.service.TemplateAndPotStatistical;
import com.qianlima.reptile.statistics.utils.DateUtil;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
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
public class TemplateAndPotStatisticalImpl extends BaseService implements TemplateAndPotStatistical {
    private static final Logger logger = LoggerFactory.getLogger(TemplateAndPotStatisticalImpl.class);


    @Autowired
    private RawdataMapper rawdataMapper;
    @Autowired
    private ModmonitorMapper modmonitorMapper;
    @Autowired
    private PotAndTmpltRepository potAndTmpltRepository;

    @Override
    public void template() {
        String today = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
        String reportStartTime = DateUtils.getLastDay(today);
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
            if (potTempMap.containsKey(t.getPotName())) {
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
                for (Integer tempId : tempList) {
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
                    potEnable++;
                } else {
                    // pot所有模板都是删除状态，则pot是废弃状态
                    if (deleteNum == tempList.size()) {
                        potAbandoned++;
                    }
                }
            }
            // 判断pot异常量
            int abnormalNum = 0;
            for (Integer tempId : tempList) {
                if (abnormalTempMap != null && abnormalTempMap.containsKey(tempId)) {
                    abnormalNum++;
                }
            }
            // 如果pot下的每一个模板都是异常状态，则pot是异常状态
            if (abnormalNum == tempList.size()) {
                potAbnormal++;
            }
        }
        // 保存统计结果
        save(reportStartTime, templtTotal, potTotal, potAbandoned, potAbnormal, potEnable, potNew);
        logger.info("selectTmpltByPot use time = {}", System.currentTimeMillis() - start);
    }

    private void save(String reportStartTime, long templtTotal, long potTotal, long potAbandoned, long potAbnormal, long potEnable, long potNew) {
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
        tmpltAndPotStatistics.setTemplateDelete(templtTotal - enableTemplt - toEnableTemplt);
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

    @Override
    public Response selectTemplate(String startTime, String endTime) {
        // 校验参数
        Response response = validateTime(startTime, endTime);
        if (response.getResult() != ResultCode.SUCCESS.getCode()){
            return response;
        }
        // 新建一个treemap，按照日期排序，方便前台渲染
        Map<String, TmpltAndPotStatistics> map = getTreeMap();
        try {
            //取月份上一个月
            String queryEndDate = queryEndDate();
            // 通过endOfTheMonth字段取出每个月最后一天的数据，endtime需要特殊处理
            List<TmpltAndPotStatistics> list = potAndTmpltRepository.queryByTimeAndMonth((startTime + DateUtils.monthStart), (queryEndDate + DateUtils.monthEnd));
            // 单独查询 endtime 最后一天的数据
            list.add(potAndTmpltRepository.queryByTime((endTime + DateUtils.monthStart), (endTime + DateUtils.monthEnd)));
            for (TmpltAndPotStatistics tp : list) {
                map.put(formatDateToMonth(tp.getQueryDate()), tp);
            }
            //检查返回结果的map，如果有月份没有数据，则补充
            fillMonth(map,startTime,endTime);
        } catch (Exception e) {
            logger.info("selectTemplate has error e ={}",e);
        }
        return Response.success(map);
    }

    /**
     * 检查返回结果的map，如果有月份没有数据，则补充这月份的key
     * @param map
     * @param startTime
     * @param endTime
     */
    private void fillMonth(Map<String, TmpltAndPotStatistics> map,String startTime, String endTime){
        //取查询月份之间的所有月份
        try {
            List<String> listMonth = DateUtils.getMonths(startTime, endTime);
            // 如果月份数量不同，则需要进行补充
            if (map.keySet().size() != listMonth.size()){
                for (String month : listMonth){
                    // 如果 结果map里不包含某个月份，则给补充一个空数组
                    if (!map.containsKey(month)){
                        map.put(month, new TmpltAndPotStatistics(month));
                    }
                }
            }
        }catch (Exception e){
            logger.info("fillMonth has error e ={}",e);
        }
    }

    /**
     *  获取endDate的前一个月份
     * @return
     */
    private String queryEndDate() {
        SimpleDateFormat format = new SimpleDateFormat(DateUtil.monthPattern);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        return format.format(m);
    }

    private Map<String, TmpltAndPotStatistics> getTreeMap() {
        Map<String, TmpltAndPotStatistics> map = new TreeMap<String, TmpltAndPotStatistics>(
                new Comparator<String>() {
                    @Override
                    public int compare(String firstDate, String secondDate) {
                        // 降序排序
                        Integer compare = Integer.parseInt(firstDate.replaceAll("-", "")) - Integer.parseInt(secondDate.replaceAll("-", ""));
                        return compare;
                    }
                });
        return map;
    }

    /**
     * 截取日期 的 yyyy-mm
     * @param date
     * @return
     */
    private String formatDateToMonth(String date) {
        if (StringUtils.isBlank(date)) {
            return date;
        }
        return date.substring(0, 7);
    }
}
