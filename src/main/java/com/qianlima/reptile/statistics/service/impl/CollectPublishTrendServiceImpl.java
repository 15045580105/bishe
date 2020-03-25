package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.constant.StatisticsConstant;
import com.qianlima.reptile.statistics.entity.PhpcmsContentStatistics;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.TrendDTO;
import com.qianlima.reptile.statistics.entity.TrendResultDTO;
import com.qianlima.reptile.statistics.repository.TrendRepository;
import com.qianlima.reptile.statistics.service.CollectPublishTrendService;
import com.qianlima.reptile.statistics.service.QianlimaStatisticsService;
import com.qianlima.reptile.statistics.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.*;

import static com.qianlima.reptile.statistics.constant.StatisticsConstant.MONGO_COLLECTION_NAME;

/**
 * @description: 采集量发布量趋势
 * @author: sx
 * @create: 2020-03-18 09:45:32
 **/
@Slf4j
@Service
public class CollectPublishTrendServiceImpl implements CollectPublishTrendService {

    @Autowired
    private TrendRepository trendRepository;

    @Autowired
    private QianlimaStatisticsService qianlimaStatisticsService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 集发布详情-年趋势
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return
     */
    @Override
    public Response getYearTrend(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return Response.error(StatisticsConstant.EORROR_PARAMETER_CANNOT_BE_EMPTY, "参数不能为空！");
        }
        YearMonth startDate = DateUtils.getYearMonth(startTime);
        YearMonth endDate = DateUtils.getYearMonth(endTime);
        if (startDate.isAfter(endDate)) {
            return Response.error(StatisticsConstant.EORROR_START_NO_LATER_THAN_END, "开始日期不能晚于结束日期！");
        }
        //两个日期相差月数
        int month = Period.between(LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1), LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1)).getMonths();
        //初始化容量，避免扩容带来的开销
        ArrayList<TrendResultDTO> resultList = new ArrayList<>(month + 1);

        for (YearMonth start = startDate; start.isBefore(endDate.plusMonths(1)) ; start = start.plusMonths(1)) {
            //当前年趋势
            List<TrendDTO> curtrendDTOS = trendRepository.queryByDate(start.toString());
            //当前年采集量
            double curCatchCount = 0.0D;
            //当前年发布量
            double curPublishCount = 0.0D;
            for (TrendDTO trendDTO :curtrendDTOS) {
                curCatchCount += StringUtils.isBlank(trendDTO.getCatchCount()) ? 0.0D : Double.parseDouble(trendDTO.getCatchCount());
                curPublishCount += StringUtils.isBlank(trendDTO.getPublishCount()) ? 0.0D : Double.parseDouble(trendDTO.getPublishCount());
            }
            //当前年的前一年趋势
            List<TrendDTO> pretrendDTOS = trendRepository.queryByDate(start.plusYears(-1).toString());
            //当前年的前一年采集量
            double preCatchCount = 0.0D;
            //当前年的前一年发布量
            double prePublishCount = 0.0D;
            for (TrendDTO trendDTO :pretrendDTOS) {
                preCatchCount += StringUtils.isBlank(trendDTO.getCatchCount()) ? 0.0D : Double.parseDouble(trendDTO.getCatchCount());
                prePublishCount += StringUtils.isBlank(trendDTO.getPublishCount()) ? 0.0D : Double.parseDouble(trendDTO.getPublishCount());
            }
            //采集量同比
            String catchProportion = CalculateUtil(new BigDecimal(curCatchCount), new BigDecimal(preCatchCount));
            //发布量同比
            String publishProportion = CalculateUtil(new BigDecimal(curPublishCount), new BigDecimal(prePublishCount));
            resultList.add(new TrendResultDTO(new TrendDTO(start.toString(), String.valueOf(curCatchCount), String.valueOf(curPublishCount)), catchProportion, publishProportion));
        }
        return Response.success(resultList);
    }

    /**
     * 求百分比，保留两位小数
     * @param a 第一操作数
     * @param b 第二操作数
     * @return
     */
    public String CalculateUtil(BigDecimal a, BigDecimal b){
        String percent =
                b == null ? "-" :
                        b.compareTo(new BigDecimal(0)) == 0 ? "-":
                                a == null ? "0.00" :
                                        a.multiply(new BigDecimal(100)).divide(b,2, BigDecimal.ROUND_HALF_UP) + "";
        return percent;
    }

    /**
     * 集发布详情-月趋势
     * @param date 当月日期
     * @return
     */
    @Override
    public Response getMonthTrend(String date) {
        if (StringUtils.isBlank(date)) {
            return Response.error(StatisticsConstant.EORROR_PARAMETER_CANNOT_BE_EMPTY, "参数不能为空！");
        }
        YearMonth yearMonth = DateUtils.getYearMonth(date);
        //当前月趋势
        List<TrendDTO> curtrendDTOS = trendRepository.queryByDate(yearMonth.toString());
        if (null == curtrendDTOS || curtrendDTOS.size() == 0) {
            return Response.error(StatisticsConstant.EORROR_NO_DATA, "暂无数据！");
        }

        //当前月趋势+同比结果,并初始化List容量
        ArrayList<TrendResultDTO> resultList = new ArrayList<>(curtrendDTOS.size());

        //当前月的前一年月趋势
        List<TrendDTO> pretrendDTOS = trendRepository.queryByDate(yearMonth.plusYears(-1).toString());
        if (null == pretrendDTOS || pretrendDTOS.size() == 0) {
            for (TrendDTO curtrendDTO : curtrendDTOS) {
                resultList.add(new TrendResultDTO(curtrendDTO, "-", "-"));
            }
            return Response.success(resultList,"去年当月暂无数据，故无采集量发布量同比为'-'！");
        }
        for (TrendDTO curtrendDTO : curtrendDTOS) {
            //每一天采集量同比
            String catchProportion = "-";
            //每一天发布量同比
            String publishProportion = "-";
            for (TrendDTO pretrendDTO: pretrendDTOS) {
                if (curtrendDTO.getCurrentDayStr().equals(DateUtils.getLocalDate(pretrendDTO.getCurrentDayStr()).plusYears(1).toString())) {
                    catchProportion = CalculateUtil(new BigDecimal(curtrendDTO.getCatchCount()), new BigDecimal(pretrendDTO.getCatchCount()));
                    publishProportion = CalculateUtil(new BigDecimal(curtrendDTO.getPublishCount()), new BigDecimal(pretrendDTO.getPublishCount()));
                    break;
                }
            }
            resultList.add(new TrendResultDTO(curtrendDTO, catchProportion, publishProportion));
        }
        return Response.success(resultList);
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Response runHistoryData (String startDate, String endDate) {
        LocalDate startLocalDate = DateUtils.getLocalDate(startDate);
        LocalDate endLocalDate = DateUtils.getLocalDate(startDate);
        for (LocalDate start = startLocalDate ; start.isBefore(endLocalDate.plusDays(1)) ; start = start.plusDays(1)) {
            int startTime = Integer.parseInt(Long.valueOf(DateUtils.convertTimeToLong(start.toString() + "00:00:00") / 1000).toString());
            int endTime = Integer.parseInt(Long.valueOf(DateUtils.convertTimeToLong(start.toString() + "23:59:59") / 1000).toString());
            //发布量
            int count =  qianlimaStatisticsService.everyDayPublishCount(startTime, endTime);
            //采集量
            int catCount = qianlimaStatisticsService.everyDayCatchCount(startTime, endTime);
            log.info("phpcmsContentCount:{},biddingRawCount:{}",count,catCount);
            //首先查询是否有当天数据
            String currentDayStr = start.toString();
            Query query = new Query(Criteria.where("currentDayStr").is(currentDayStr));
            long mongoCount = 0;
            try{
                mongoCount = mongoTemplate.count(query, String.class, MONGO_COLLECTION_NAME);
            }catch (Exception e){
                log.error(e.getMessage());
            }
            if(mongoCount == 0){
                Map<String,Object> mongoMap = new HashMap<>(8);
                mongoMap.put("catchCount", catCount);
                mongoMap.put("publishCount", count);
                mongoMap.put("updateTime", System.currentTimeMillis());
                mongoMap.put("currentDayTime", startTime);
                mongoMap.put("currentDayStr", currentDayStr);
                mongoTemplate.insert(mongoMap, MONGO_COLLECTION_NAME);
                log.info("{} count is not exist,mongo inserted!", currentDayStr);
            }
        }
            return Response.success("SUCCESS");
    }
}
