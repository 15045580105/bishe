package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.TrendDTO;
import com.qianlima.reptile.statistics.entity.TrendResultDTO;
import com.qianlima.reptile.statistics.repository.TrendRepository;
import com.qianlima.reptile.statistics.service.CollectPublishTrendService;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 采集量发布量趋势
 * @author: sx
 * @create: 2020-03-18 09:45:32
 **/
@Service
public class CollectPublishTrendServiceImpl implements CollectPublishTrendService {
    @Autowired
    private TrendRepository trendRepository;

    @Override
    public Response getYearTrend(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return Response.error(100, "参数不能为空！");
        }
        YearMonth startDate = DateUtils.getLocalDate(startTime);
        YearMonth endDate = DateUtils.getLocalDate(endTime);
        if (startDate.isAfter(endDate)) {
            return Response.error(101, "开始日期不能晚于结束日期！");
        }
        ArrayList<TrendResultDTO> resultLists = new ArrayList<>();
        for (YearMonth start = startDate; start.isBefore(endDate.plusMonths(1)) ; start = start.plusMonths(1)) {
            //当前年趋势
            List<TrendDTO> curtrendDTOS = trendRepository.queryByDate(start.toString());
            double curCatchCount = 0.0D;
            double curPublishCount = 0.0D;
            for (TrendDTO trendDTO :curtrendDTOS) {
                curCatchCount += StringUtils.isBlank(trendDTO.getCatchCount()) ? 0.0D : Double.parseDouble(trendDTO.getCatchCount());
                curPublishCount += StringUtils.isBlank(trendDTO.getPublishCount()) ? 0.0D : Double.parseDouble(trendDTO.getPublishCount());
            }
            //当前年的前一年趋势
            List<TrendDTO> pretrendDTOS = trendRepository.queryByDate(start.plusYears(-1).toString());
            double preCatchCount = 0.0D;
            double prePublishCount = 0.0D;
            for (TrendDTO trendDTO :pretrendDTOS) {
                preCatchCount += StringUtils.isBlank(trendDTO.getCatchCount()) ? 0.0D : Double.parseDouble(trendDTO.getCatchCount());
                prePublishCount += StringUtils.isBlank(trendDTO.getPublishCount()) ? 0.0D : Double.parseDouble(trendDTO.getPublishCount());
            }
            //采集量同比
            String catchProportion = CalculateUtil(new BigDecimal(curCatchCount), new BigDecimal(preCatchCount));
            //发布量同比
            String publishProportion = CalculateUtil(new BigDecimal(curPublishCount), new BigDecimal(prePublishCount));
            resultLists.add(new TrendResultDTO(new TrendDTO(start.toString(), String.valueOf(curCatchCount), String.valueOf(curPublishCount)), catchProportion, publishProportion));
        }
        return Response.success(resultLists);
    }

    /**
     * 求百分比，保留两位小数
     * @param a
     * @param b
     * @return
     */
    public String CalculateUtil(BigDecimal a, BigDecimal b){
        String percent =
                b == null ? "-" :
                        b.compareTo(new BigDecimal(0)) == 0 ? "-":
                                a == null ? "0.00%" :
                                        a.multiply(new BigDecimal(100)).divide(b,2, BigDecimal.ROUND_HALF_UP) + "%";
        return percent;
    }

}
