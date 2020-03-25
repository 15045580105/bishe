package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.domain.TraceStatistic;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.TraceStatisticResponse;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.mapper.RawdatasMapper;
import com.qianlima.reptile.statistics.repository.TraceStatisticRepository;
import com.qianlima.reptile.statistics.service.TraceStatisticService;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: TraceStatisticServiceImpl
 * @date 2020/3/18 11:54 上午
 */
@Service
public class TraceStatisticServiceImpl implements TraceStatisticService {
    @Autowired
    private ModmonitorMapper modmonitorMapper;
    @Autowired
    private RawdatasMapper rawdatasMapper;
    @Autowired
    private QianlimaMapper qianlimaMapper;
    @Autowired
    private TraceStatisticRepository traceStatisticRepository;

    Map<String, String> octopusList = new HashMap<>();
    Map<String, String> brigeList = new HashMap<>();
    Map<String, String> peerList = new HashMap<>();
    Map<String, String> editList = new HashMap<>();
    Map<String, String> mainList = new HashMap<>();

    private static HashMap<String, String> orgUrls = new HashMap<>();
    static {
        orgUrls.put("八爪鱼", "bridge/octopus_list");
        orgUrls.put("桥接页面", "bridge/bidding_list");
        orgUrls.put("竞品", "bridge/peer_list");
        orgUrls.put("人工编辑", "column/datalist_n.jsp");
    }

    @Override
    public Response getTraceStatistic(String startTime, String endTime) {
        if (!startTime.equals(endTime)) {
            Map<String, Integer> map = traceStatisticRepository.queryEachTotalCountInTime(startTime,endTime);
            List<TraceStatistic> biddingStatistics = traceStatisticRepository.queryInTime(startTime, endTime, 1);
            TraceStatistic biddingStatistic = sumEachField(biddingStatistics,1);
            biddingStatistic.setTotalCount(map.get("采集量"));
            List<TraceStatistic>  phpStatistics = traceStatisticRepository.queryInTime(startTime, endTime, 0);
            TraceStatistic phpStatistic = sumEachField(phpStatistics,0);
            phpStatistic.setTotalCount(map.get("发布量"));
            TraceStatisticResponse traceStatisticResponse = new TraceStatisticResponse();
            traceStatisticResponse.setCollectCount(biddingStatistic);
            traceStatisticResponse.setReleaseCount(phpStatistic);

            return Response.success(traceStatisticResponse);
        }
        if (startTime.equals(endTime)) {
            Map<String, Integer> map = traceStatisticRepository.queryEachTotalCountByTime(startTime);
            TraceStatistic biddingStatistic = traceStatisticRepository.queryByTime(startTime, 1).get(0);
            biddingStatistic.setTotalCount(map.get("采集量"));
            TraceStatistic phpStatistic = traceStatisticRepository.queryByTime(startTime, 0).get(0);
            phpStatistic.setTotalCount(map.get("发布量"));
            TraceStatisticResponse traceStatisticResponse = new TraceStatisticResponse();
            traceStatisticResponse.setCollectCount(biddingStatistic);
            traceStatisticResponse.setReleaseCount(phpStatistic);
            return Response.success(traceStatisticResponse);

        }
        return null;
    }


    @Override
    public void saveStatistic(Long startTime, Long endTime) {
        LoadDatas();

        System.err.println(DateUtils.getFormatDateStrBitAdd(startTime, DateUtils.FUZSDF));

        Map<String, Integer> map = traceStatisticRepository.queryEachTotalCountInTime(DateUtils.getFormatDateStrBitAdd(startTime, DateUtils.FUZSDF)
                , DateUtils.getFormatDateStrBitAdd(endTime, DateUtils.FUZSDF));
        System.err.println("map select complete");
        TraceStatistic biddingCount = getDailyBiddingCount(startTime, endTime);
        biddingCount.setTotalCount(map.get("采集量"));
        TraceStatistic phpCount = getDailyPhpCount(startTime, endTime);
        phpCount.setTotalCount(map.get("发布量"));
        System.err.println("采集量" + biddingCount.toString());
        System.err.println("发布量" + phpCount.toString());

        traceStatisticRepository.save(biddingCount);
        traceStatisticRepository.save(phpCount);
    }

    @Override
    public void runData(Long startTime, Long endTime) {
        Long currentStartTime = startTime;
        Long currentEndTime = currentStartTime + 86399L;
        while (currentEndTime <= endTime) {
            saveStatistic(currentStartTime,currentEndTime);
            currentStartTime += 86400;
            currentEndTime += 86400;
        }
    }

    /**
     * @Title LoadDatas
     * @Description 载入分类数据
     * @Author liuchanglin
     * @Date 2020/3/25 7:46 下午
     * @Param []
     * @return void
     **/
    private void LoadDatas() {
        System.err.println("load orgin datas");
        octopusList = convertToMap(selectByPageTypes(orgUrls.get("八爪鱼"), 0));
        System.err.println("octopusList complete");
        brigeList = convertToMap(selectByPageTypes(orgUrls.get("桥接页面"), 0));
        System.err.println("brigeList complete");
        peerList = convertToMap(selectByPageTypes(orgUrls.get("竞品"), 0));
        System.err.println("peerList complete");
        editList = convertToMap(selectByPageTypes(orgUrls.get("人工编辑"), 0));
        System.err.println("editList complete");
        mainList = convertToMap(selectByPageTypes("", 1));
        System.err.println("mainList complete");
    }
    /**
     * @Title convertToMap
     * @Description List转map(key)
     * @Author liuchanglin
     * @Date 2020/3/25 7:46 下午
     * @Param [templt]
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    public Map<String, String> convertToMap(List<String> templt) {
        Map<String, String>  teamMap = new HashMap<>();
        for (String t : templt){
            teamMap.put(t,null);
        }
        return teamMap;
    }
    /**
     * @return java.lang.Integer
     * @Title selectByPage
     * @Description 查询orgUrl对应id list
     * flag
     * 0-分类
     * 1-主爬虫
     * @Author liuchanglin
     * @Date 2020/3/18 3:42 下午
     * @Param [orgUrl, flag]
     **/
    private List<String> selectByPageTypes(String orgUrl, int flag) {
        if (flag == 0) {
            int limit = 0;

            List<String> list = modmonitorMapper.selectCrawlconfigByOrgUrl(orgUrl, limit);
            List<String> ids = new ArrayList<>();
            while (list != null && list.size() != 0) {
                ids.addAll(list);
                limit += 1000;
                list = modmonitorMapper.selectCrawlconfigByOrgUrl(orgUrl, limit);
            }
            return ids;
        }

        if (flag == 1) {
            int limit = 0;
            List<String> list = modmonitorMapper.selectCrawlconfigMainCraw(limit);
            List<String> ids = new ArrayList<>();
            while (list != null && list.size() != 0) {
                ids.addAll(list);
                limit += 1000;
                list = modmonitorMapper.selectCrawlconfigMainCraw(limit);
            }
            return ids;
        }
        return null;
    }
    /**
     * @Title selectByPageCounts
     * @Description 查询每日模版id
     * @Author liuchanglin
     * @Date 2020/3/25 7:47 下午
     * @Param [flag, startTime, endTime]
     * @return java.util.List<java.lang.String>
     **/
    private List<String> selectByPageCounts(int flag, Long startTime, Long endTime) {
        if (flag == 0) {
            List<String> list = qianlimaMapper.selectTmpltIdInTime(startTime, endTime);
            return list;
        }
        if (flag == 1) {
            List<String> list = rawdatasMapper.selectTemplateIdInTime(startTime, endTime);
            return list;
        }
        return null;
    }
    /**
     * @Title getDailyPhpCount
     * @Description 对比查询 （发布量）
     * @Author liuchanglin
     * @Date 2020/3/25 7:47 下午
     * @Param [startTime, endTime]
     * @return com.qianlima.reptile.statistics.domain.TraceStatistic
     **/
    private TraceStatistic getDailyPhpCount(Long startTime, Long endTime) {
        System.err.println("get daily phpCount");
        TraceStatistic traceStatisticPhp = new TraceStatistic();
        traceStatisticPhp.setQueryDate(DateUtils.getFormatDateStrBitAdd(startTime, DateUtils.FUZSDF));
        traceStatisticPhp.setType(0);
        List<String> templatePHPIDs = selectByPageCounts(0, startTime, endTime);
        int octopus = 0;
        int brige = 0;
        int edit = 0;
        int main = 0;
        int peer = 0;
        if (templatePHPIDs != null && templatePHPIDs.size() != 0) {
            System.err.println("cycle phpCount");
            for (String templateBiddingID : templatePHPIDs) {
                if (StringUtils.isNotBlank(templateBiddingID)) {
                    if (octopusList.containsKey(templateBiddingID)) {
                        octopus++;
                    } else if (brigeList.containsKey(templateBiddingID)) {
                        brige++;
                    } else if (editList.containsKey(templateBiddingID)) {
                        edit++;
                    } else if (mainList.containsKey(templateBiddingID)) {
                        main++;
                    } else if (peerList.containsKey(templateBiddingID)) {
                        peer++;
                    }
                }
            }
            traceStatisticPhp.setOctopusCount(octopus);
            traceStatisticPhp.setBridgePageCount(brige);
            traceStatisticPhp.setCompeteProductsCount(peer);
            traceStatisticPhp.setArtificialEditCount(edit);
            traceStatisticPhp.setMainCrawlerCount(main);
        }
        return traceStatisticPhp;
    }
    /**
     * @Title getDailyBiddingCount
     * @Description 对比查询（采集量）
     * @Author liuchanglin
     * @Date 2020/3/25 7:48 下午
     * @Param [startTime, endTime]
     * @return com.qianlima.reptile.statistics.domain.TraceStatistic
     **/
    private TraceStatistic getDailyBiddingCount(Long startTime, Long endTime) {
        System.err.println("geting dailyBidding count");
        TraceStatistic traceStatisticBidding = new TraceStatistic();
        traceStatisticBidding.setQueryDate(DateUtils.getFormatDateStrBitAdd(startTime, DateUtils.FUZSDF));
        traceStatisticBidding.setType(1);
        List<String> templateBiddingIDs = selectByPageCounts(1, startTime, endTime);
        int octopus = 0;
        int brige = 0;
        int edit = 0;
        int main = 0;
        int peer = 0;
        if (templateBiddingIDs != null && templateBiddingIDs.size() != 0) {
            System.err.println("cycle biddingCount");
            for (String templateBiddingID : templateBiddingIDs) {
                if (StringUtils.isNotBlank(templateBiddingID)) {
                    if (octopusList.containsKey(templateBiddingID)) {
                        octopus++;
                    } else if (brigeList.containsKey(templateBiddingID)) {
                        brige++;
                    } else if (editList.containsKey(templateBiddingID)) {
                        edit++;
                    } else if (mainList.containsKey(templateBiddingID)) {
                        main++;
                    } else if (peerList.containsKey(templateBiddingID)) {
                        peer++;
                    }
                }
            }
            traceStatisticBidding.setOctopusCount(octopus);
            traceStatisticBidding.setBridgePageCount(brige);
            traceStatisticBidding.setCompeteProductsCount(peer);
            traceStatisticBidding.setArtificialEditCount(edit);
            traceStatisticBidding.setMainCrawlerCount(main);
        }
        return traceStatisticBidding;
    }
    /**
     * @Title sumEachField
     * @Description 发布量采集量按时间范围统计计算出个字段总和
     * @Author liuchanglin
     * @Date 2020/3/23 6:39 下午
     * @Param [list, type]
     * @return com.qianlima.reptile.statistics.domain.TraceStatistic
     **/
    private TraceStatistic sumEachField(List<TraceStatistic> list,Integer type) {
        TraceStatistic newTraceStatistic = new TraceStatistic();
        newTraceStatistic.setQueryDate(DateUtils.getFormatDateStr(System.currentTimeMillis(), DateUtils.FUZSDF));
        newTraceStatistic.setType(type);
        newTraceStatistic.setMainCrawlerCount(0);
        newTraceStatistic.setArtificialEditCount(0);
        newTraceStatistic.setBridgePageCount(0);
        newTraceStatistic.setCompeteProductsCount(0);
        newTraceStatistic.setOctopusCount(0);
        for (TraceStatistic traceStatistic : list) {
            newTraceStatistic.setMainCrawlerCount(newTraceStatistic.getMainCrawlerCount() + traceStatistic.getMainCrawlerCount());
            newTraceStatistic.setArtificialEditCount(newTraceStatistic.getArtificialEditCount() + traceStatistic.getArtificialEditCount());
            newTraceStatistic.setBridgePageCount(newTraceStatistic.getBridgePageCount() + traceStatistic.getBridgePageCount());
            newTraceStatistic.setCompeteProductsCount(newTraceStatistic.getCompeteProductsCount() + traceStatistic.getCompeteProductsCount());
            newTraceStatistic.setOctopusCount(newTraceStatistic.getOctopusCount() + traceStatistic.getOctopusCount());

        }
        return newTraceStatistic;
    }

}
