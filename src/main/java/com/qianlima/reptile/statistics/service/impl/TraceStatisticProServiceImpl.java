package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.domain.TraceStatistic;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.mapper.ModmonitorMapper;
import com.qianlima.reptile.statistics.mapper.QianlimaMapper;
import com.qianlima.reptile.statistics.mapper.RawdatasMapper;
import com.qianlima.reptile.statistics.repository.TraceStatisticRepository;
import com.qianlima.reptile.statistics.service.TraceStatisticProService;
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
 * @ClassName: TraceStatisticProServiceImpl
 * @date 2020/3/24 11:44 下午
 */
@Service
public class TraceStatisticProServiceImpl implements TraceStatisticProService {
    private static HashMap<String, String> orgUrls = new HashMap<>();

    static {
//        orgUrls.put("微信", "bridge/wechat_list");
        orgUrls.put("八爪鱼", "bridge/octopus_list");
        orgUrls.put("桥接页面", "bridge/bidding_list");
        orgUrls.put("竞品", "bridge/peer_list");
        orgUrls.put("人工编辑", "column/datalist_n.jsp");
    }

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


    @Override
    public Response getTraceStatistic(String startTime, String endTime) {

        return null;
    }

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

    @Override
    public void saveStatistic() {
        LoadDatas();

        Long startTime = 1563206400L;
        Long endTime = 1563292799L;

        while (endTime <= 1585065599) {

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
            startTime += 86400;
            endTime += 86400;

            traceStatisticRepository.save(biddingCount);
            traceStatisticRepository.save(phpCount);

        }


    }


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
     * @Description 分页查询orgUrl对应id list
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


    private TraceStatistic getDailyPhpCount(Long startTime, Long endTime) {
        System.err.println("get daily phpCount");
        TraceStatistic traceStatisticPhp = new TraceStatistic();
        traceStatisticPhp.setQueryDate(DateUtils.getFormatDateStrBitAdd(startTime, DateUtils.FUZSDF));
        traceStatisticPhp.setType(0);
//        TraceStatistic traceStatisticBid = new TraceStatistic();
        List<String> templatePHPIDs = selectByPageCounts(0, startTime, endTime);
//        List<String> templateBiddingIDs = rawdatasMapper.selectTemplateIdInTime(startTime, endTime);
        int octopus = 0;
        int brige = 0;
        int edit = 0;
        int main = 0;
        int peer = 0;
        if (templatePHPIDs != null && templatePHPIDs.size() != 0) {
            System.err.println("cycle 1");
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
            System.err.println("cycle 3");
            traceStatisticPhp.setCompeteProductsCount(peer);
            traceStatisticPhp.setArtificialEditCount(edit);
            System.err.println("cycle 5");
            traceStatisticPhp.setMainCrawlerCount(main);
        }
        return traceStatisticPhp;
    }

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
            System.err.println("cycle 1");
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
            System.err.println("cycle 3");
            traceStatisticBidding.setCompeteProductsCount(peer);
            traceStatisticBidding.setArtificialEditCount(edit);
            System.err.println("cycle 5");
            traceStatisticBidding.setMainCrawlerCount(main);
        }
        return traceStatisticBidding;
    }
}
