package com.qianlima.reptile.statistics.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 00:00 2020-03-19
 * @Modified By :
 */


import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.RawdataMapper;
import com.qianlima.reptile.statistics.repository.PotAndTmpltRepository;
import com.qianlima.reptile.statistics.service.TemplateAndPotStatistical;
import com.qianlima.reptile.statistics.utils.DateUtil;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author gyx
 * @date 2020-03-19 00:00
 */
@Service
public class TemplateAndPotStatisticalImpl implements TemplateAndPotStatistical {
    @Autowired
    private RawdataMapper rawdataMapper;
    @Autowired
    private ModmonitorMapper modmonitorMapper;
    @Autowired
    private PotAndTmpltRepository potAndTmpltRepository;

    @Override
    public void template() {
        String reportStartTime = DateUtil.getDateTime(DateUtil.getDatePattern(), new Date());
        String startTime = reportStartTime + " 00:00:00";
        String endTime = reportStartTime + " 23:59:59";
        Map<Object, Object> mapTmplt = rawdataMapper.selectTemplt();
        List<String> list = modmonitorMapper.selectFailTemplt(startTime, endTime);
        TmpltAndPotStatistics tmpltAndPotStatistics = new TmpltAndPotStatistics();
        long potTotal = rawdataMapper.selectPotTotalCount();
        long templtTotal = rawdataMapper.selectTempltTotal();
        long enableTemplt = rawdataMapper.selectEnableTemplt();
        long toEnableTemplt = rawdataMapper.selectToEnableTemplt();
        long unclassifiedTemplt = rawdataMapper.selectUnclassifiedTemplt();
        long failTemplt = modmonitorMapper.selectFailTempltCount(startTime, endTime);
        long endOfTheMonth = 0;
        long k = potTotal / 500;
        int potEnable = 0, potAbandoned = 0, potNew = 0, potAbnormal = 0;
        for (int i = 0; i <= k; i++) {
            List<String> list1 = rawdataMapper.selectPotByPage(i + "", "500");
            for (int j = 0; j < list1.size(); j++) {
                List<String> list2 = rawdataMapper.selectTmpltByPot(list1.get(j));
                Boolean x = null;
                if (list2.size() == 0) {
                    potNew++;
                } else {
                    for (int l = 0; l < list2.size(); l++) {
                        if (mapTmplt.get(list2.get(l)).equals("1")) {
                            x = true;
                            break;
                        }
                        if (!mapTmplt.get(list2.get(l)).equals("0")) {
                            x = false;
                        }
                    }
                }
                if (x) {
                    potEnable++;
                } else {
                    potAbandoned++;
                }

                boolean y = true;
                if (list2.size() != 0) {
                    for (int l = 0; l < list2.size(); l++) {
                        if (!list.contains(list2.get(l))) {
                            y = false;
                            break;
                        }
                    }
                }
                if (y) {
                    potAbnormal++;
                }
            }
        }

        if (DateUtils.isLastDayOfMonth(DateUtils.parseDateFromDateStr(reportStartTime))) {
            endOfTheMonth = 1;
        }
        tmpltAndPotStatistics.setEndOfTheMonth(endOfTheMonth);
        tmpltAndPotStatistics.setPotTotal(potTotal);
        tmpltAndPotStatistics.setTemplateTotal(templtTotal);
        tmpltAndPotStatistics.setTemplateUsing(enableTemplt);
        tmpltAndPotStatistics.setTemplatToEnable(toEnableTemplt);
        tmpltAndPotStatistics.setTemplateAbnormal(failTemplt);
        tmpltAndPotStatistics.setTemplateDelete(unclassifiedTemplt);
        tmpltAndPotStatistics.setPotAbandoned(potAbandoned);
        tmpltAndPotStatistics.setPotAbnormal(potAbnormal);
        tmpltAndPotStatistics.setPotNew(potNew);
        tmpltAndPotStatistics.setPotUsing(potEnable);
        tmpltAndPotStatistics.setQueryDate(reportStartTime);
        potAndTmpltRepository.save(tmpltAndPotStatistics);
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
                mon=endTime;
            }
            List<TmpltAndPotStatistics> list = potAndTmpltRepository.queryByTimeAndMonth(startTime, mon);
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).getQueryDate(), list.get(i));
            }
        }
        List<TmpltAndPotStatistics> list1= potAndTmpltRepository.queryByTime((startTime+"01"),(endTime+"31"));
        map.put(list1.get(0).getQueryDate(), list1.get(0));
        return map;
    }

}
