package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.constant.StatisticsConstant;
import com.qianlima.reptile.statistics.domain.TraceStatistic;
import com.qianlima.reptile.statistics.entity.PublishRateDto;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.repository.TraceStatisticRepository;
import com.qianlima.reptile.statistics.service.BaseService;
import com.qianlima.reptile.statistics.service.PublishRateService;
import com.qianlima.reptile.statistics.utils.MathUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PublishRateServiceImpl extends BaseService implements PublishRateService {

    @Autowired
    private TraceStatisticRepository traceStatisticRepository;

    @Override
    public Response queryPublishRate(String startDate, String endDate) {
        // 统计发布量
        List<TraceStatistic> publishList = traceStatisticRepository.queryInTime(startDate, endDate);
        Map<String, List<TraceStatistic>> publishMap = groupPublishList(publishList, startDate, endDate);
        Set<String> publishSet = publishMap.keySet();
        // 构建各个来源map
        Map<String, List<PublishRateDto>> sourceMap = buildSourceMap();
        for (String month : publishSet) {
            // 发布量 每月统计
            int competeProductPublish = 0;
            int bridgePagePublish = 0;
            int manualEditPublish = 0;
            int octopusPublish = 0;
            int mainCrawlerPublish = 0;
            int webchaPublish = 0;
            // 采集量 每月统计
            int competeProductCollect = 0;
            int bridgePageCollect = 0;
            int manualEditCollect = 0;
            int octopusCollect = 0;
            int mainCrawlerCollect = 0;
            int webchaCollect = 0;
            List<TraceStatistic> traceStatistics = publishMap.get(month);
            for (TraceStatistic t : traceStatistics) {
                //如果是发布数据
                if (t.getType() == StatisticsConstant.PUBLISH) {
                    competeProductPublish += t.getCompeteProductsCount();
                    bridgePagePublish += t.getBridgePageCount();
                    manualEditPublish += t.getArtificialEditCount();
                    octopusPublish += t.getOctopusCount();
                    mainCrawlerPublish += t.getMainCrawlerCount();
                } else {
                    // 如果是采集数据
                    competeProductCollect += t.getCompeteProductsCount();
                    bridgePageCollect += t.getBridgePageCount();
                    manualEditCollect += t.getArtificialEditCount();
                    octopusCollect += t.getOctopusCount();
                    mainCrawlerCollect += t.getMainCrawlerCount();
                }
            }
            sourceMap.get(StatisticsConstant.competeProduct).add(new PublishRateDto(month, competeProductPublish, competeProductCollect, MathUtil.divi(competeProductPublish, competeProductCollect)));
            sourceMap.get(StatisticsConstant.bridgePage).add(new PublishRateDto(month, bridgePagePublish, bridgePageCollect, MathUtil.divi(bridgePagePublish, bridgePageCollect)));
            sourceMap.get(StatisticsConstant.manualEdit).add(new PublishRateDto(month, manualEditPublish, manualEditCollect, MathUtil.divi(manualEditPublish, manualEditCollect)));
            sourceMap.get(StatisticsConstant.octopus).add(new PublishRateDto(month, octopusPublish, octopusCollect, MathUtil.divi(octopusPublish, octopusCollect)));
            sourceMap.get(StatisticsConstant.mainCrawler).add(new PublishRateDto(month, mainCrawlerPublish, mainCrawlerCollect, MathUtil.divi(mainCrawlerPublish, mainCrawlerCollect)));
            sourceMap.get(StatisticsConstant.webcha).add(new PublishRateDto(month, webchaPublish, webchaCollect, MathUtil.divi(webchaPublish, webchaCollect)));
        }

        return Response.success(sourceMap);
    }

    /**
     * 按照月份对数据进行分类
     *
     * @param publishList
     * @return
     */
    private Map<String, List<TraceStatistic>> groupPublishList(List<TraceStatistic> publishList, String startDate, String endDate) {
        Map<String, List<TraceStatistic>> treeMap = getTreeMap();
        Map<String, List<TraceStatistic>> publishMap = fillMonth(treeMap, startDate, endDate);
        for (TraceStatistic t : publishList) {
            String queryDate = subMonth(t.getQueryDate());
            if (publishMap.containsKey(queryDate)) {
                publishMap.get(queryDate).add(t);
            } else {
                List<TraceStatistic> traceList = new ArrayList<>();
                traceList.add(t);
                publishMap.put(queryDate, traceList);
            }
        }
        return publishMap;
    }


    /**
     * 检查返回结果的map，如果有月份没有数据，则补充这月份的key
     *
     * @param map
     * @param startTime
     * @param endTime
     */
    private Map<String, List<TraceStatistic>> fillMonth(Map<String, List<TraceStatistic>> map, String startTime, String endTime) {
        //取查询月份之间的所有月份
        List<String> listMonth = getMonths(startTime, endTime);
        // 如果月份数量不同，则需要进行补充
        if (map.keySet().size() != listMonth.size()) {
            for (String month : listMonth) {
                // 如果 结果map里不包含某个月份，则给补充一个空数组
                if (!map.containsKey(month)) {
                    map.put(month, new ArrayList<>());
                }
            }
        }
        return map;
    }

    private Map<String, List<TraceStatistic>> getTreeMap() {
        Map<String, List<TraceStatistic>> map = new TreeMap<String, List<TraceStatistic>>(
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

    private Map<String, List<PublishRateDto>> buildSourceMap() {
        HashMap<String, List<PublishRateDto>> sourceMap = new HashMap<>();
        for (String source : StatisticsConstant.DataSourceList) {
            sourceMap.put(source, new ArrayList<PublishRateDto>());
        }
        return sourceMap;
    }

    /**
     * 截取至月份，例如 2020-01-01 截取后得 2020-01
     *
     * @param queryDate
     * @return
     */
    private static String subMonth(String queryDate) {
        if (StringUtils.isNotBlank(queryDate)) {
            return queryDate.substring(0, 7);
        } else {
            return queryDate;
        }
    }

}
