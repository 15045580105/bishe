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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private static HashMap<String, String> orgUrls = new HashMap<>();
    static {
        orgUrls.put("微信", "bridge/wechat_list");
        orgUrls.put("八爪鱼", "bridge/octopus_list");
        orgUrls.put("桥接页面", "bridge/bidding_list");
        orgUrls.put("竞品", "bridge/peer_list");
        orgUrls.put("人工编辑", "column/datalist_n.jsp");
    }

    private int count = 0;
    @Override
    public Response getTraceStatistic(String startTime, String endTime) {
        if (!startTime.equals(endTime)) {
            List<TraceStatistic> biddingStatistics = traceStatisticRepository.queryInTime(startTime, endTime, 1);
            TraceStatistic biddingStatistic = sumEachField(biddingStatistics,1);
            List<TraceStatistic>  phpStatistics = traceStatisticRepository.queryInTime(startTime, endTime, 0);
            TraceStatistic phpStatistic = sumEachField(phpStatistics,0);
            TraceStatisticResponse traceStatisticResponse = new TraceStatisticResponse();
            traceStatisticResponse.setCollectCount(biddingStatistic);
            traceStatisticResponse.setReleaseCount(phpStatistic);

            return Response.success(traceStatisticResponse);
        }
        if (startTime.equals(endTime)) {
            TraceStatistic biddingStatistic = traceStatisticRepository.queryByTime(startTime, 1).get(0);
            TraceStatistic phpStatistic = traceStatisticRepository.queryByTime(startTime, 0).get(0);
            TraceStatisticResponse traceStatisticResponse = new TraceStatisticResponse();
            traceStatisticResponse.setCollectCount(biddingStatistic);
            traceStatisticResponse.setReleaseCount(phpStatistic);
            return Response.success(traceStatisticResponse);

        }
        return null;
    }

    @Override
    public void saveStatistic() {
//                采集量bidding_raw
        TraceStatistic biddingStatistic = new TraceStatistic();
        biddingStatistic.setType(1);
        biddingStatistic.setQueryDate(DateUtils.getFormatDateStrBitAdd(DateUtils.getYesterTodayEndTime(), DateUtils.FUZSDF));
        biddingStatistic.setWeChatCount(selectByPage(orgUrls.get("微信"), 0));
        biddingStatistic.setOctopusCount(selectByPage(orgUrls.get("八爪鱼"), 0));
        biddingStatistic.setBridgePageCount(selectByPage(orgUrls.get("桥接页面"), 0));
        biddingStatistic.setCompeteProductsCount(selectByPage(orgUrls.get("竞品"), 0));
        biddingStatistic.setArtificialEditCount(selectByPage(orgUrls.get("人工编辑"), 0));
        biddingStatistic.setMainCrawlerCount(selectByPage("",2));
        traceStatisticRepository.save(biddingStatistic);
        //        发布量phpcms_content
        TraceStatistic phpStatistic = new TraceStatistic();
        phpStatistic.setType(0);
        phpStatistic.setQueryDate(DateUtils.getFormatDateStrBitAdd(DateUtils.getYesterTodayEndTime(), DateUtils.FUZSDF));
        phpStatistic.setWeChatCount(selectByPage(orgUrls.get("微信"), 1));
        phpStatistic.setOctopusCount(selectByPage(orgUrls.get("八爪鱼"), 1));
        phpStatistic.setBridgePageCount(selectByPage(orgUrls.get("桥接页面"), 1));
        phpStatistic.setCompeteProductsCount(selectByPage(orgUrls.get("竞品"), 1));
        phpStatistic.setArtificialEditCount(selectByPage(orgUrls.get("人工编辑"), 1));
        phpStatistic.setMainCrawlerCount(selectByPage("",3));
        refreshCount();
        traceStatisticRepository.save(phpStatistic);

    }

    /**
     * @Title selectByPage
     * @Description 分页查询orgUrl对应id list
     *              flag
     *              0-bidingraw
     *              1-phpcms
     *              2-主爬虫-bidingraw
     *              3-主爬虫-phpcms_content
     * @Author liuchanglin
     * @Date 2020/3/18 3:42 下午
     * @Param [orgUrl, flag]
     * @return java.lang.Integer
     **/
    private Integer selectByPage(String orgUrl,int flag) {
        refreshCount();
        if (flag == 0) {
            int limit = 0;
            List<String> list = modmonitorMapper.selectCrawlconfigByOrgUrl(orgUrl, limit);
            List<String> ids = new ArrayList<>();
            while (list != null && list.size() != 0) {
                ids.addAll(list);
                limit += 1000;
                list = modmonitorMapper.selectCrawlconfigByOrgUrl(orgUrl, limit);
            }
            getCount(ids, DateUtils.getYesterTodayStartTime(), DateUtils.getYesterTodayEndTime(),0);
            return count;
        }
        if (flag == 1) {
                int limit = 0;
                List<String> list = modmonitorMapper.selectCrawlconfigByOrgUrl(orgUrl, limit);
                List<String> ids = new ArrayList<>();
                while (list != null && list.size() != 0) {
                    ids.addAll(list);
                    limit += 1000;
                    list = modmonitorMapper.selectCrawlconfigByOrgUrl(orgUrl, limit);
                }
                getCount(ids, DateUtils.getYesterTodayStartTime(), DateUtils.getYesterTodayEndTime(),1);
                return count;
            }
        if (flag == 2) {
            int limit = 0;
            List<String> list = modmonitorMapper.selectCrawlconfigMainCraw(limit);
            List<String> ids = new ArrayList<>();
            while (list != null && list.size() != 0) {
                ids.addAll(list);
                limit += 1000;
                list = modmonitorMapper.selectCrawlconfigMainCraw(limit);
            }
            getCount(ids, DateUtils.getYesterTodayStartTime(), DateUtils.getYesterTodayEndTime(),0);
            return count;
        }
        if (flag == 3) {
            int limit = 0;
            List<String> list = modmonitorMapper.selectCrawlconfigMainCraw(limit);
            List<String> ids = new ArrayList<>();
            while (list != null && list.size() != 0) {
                ids.addAll(list);
                limit += 1000;
                list = modmonitorMapper.selectCrawlconfigMainCraw(limit);
            }
            getCount(ids, DateUtils.getYesterTodayStartTime(), DateUtils.getYesterTodayEndTime(),1);
            return count;
        }
        return null;
    }


    /**
     * @Title getCount
     * @Description 递归对id进行in查询 每次查500
     *              flag 0-bidding_raw 1-phpcms_content
     * @Author liuchanglin
     * @Date 2020/3/18 3:44 下午
     * @Param [ids, startTime, endTime, flag]
     * @return void
     **/
    private void getCount(List<String> ids,Long startTime,Long endTime,Integer flag) {
        if (flag == 0) {
            if (ids != null && ids.size() != 0) {
                if (ids.size() > 1000) {
                    count += rawdatasMapper.selectBiddingCountsByIds(ids.subList(0, 500), startTime, endTime);
                    getCount(ids.subList(500, ids.size()), startTime, endTime,0);
                } else {
                    count += rawdatasMapper.selectBiddingCountsByIds(ids,startTime,endTime);
                }
            }
        }
        if (flag == 1) {
            if (ids != null && ids.size() != 0) {
                if (ids.size() > 1000) {
                    count+=qianlimaMapper.selectPhpcmsCountsByIds(ids.subList(0, 500), startTime, endTime);
                    getCount(ids.subList(500, ids.size()), startTime, endTime,1);
                } else {
                    count+=qianlimaMapper.selectPhpcmsCountsByIds(ids,startTime,endTime);
                }
            }
        }

    }


    /**
     * @Title refreshCount
     * @Description 重置变量 count 为 0
     * @Author liuchanglin
     * @Date 2020/3/18 3:56 下午
     * @Param []
     * @return void
     **/
    private void refreshCount() {
        System.err.println(count);
        count = 0;
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
        newTraceStatistic.setWeChatCount(0);
        newTraceStatistic.setArtificialEditCount(0);
        newTraceStatistic.setBridgePageCount(0);
        newTraceStatistic.setCompeteProductsCount(0);
        newTraceStatistic.setOctopusCount(0);
        for (TraceStatistic traceStatistic : list) {
            newTraceStatistic.setMainCrawlerCount(newTraceStatistic.getMainCrawlerCount() + traceStatistic.getMainCrawlerCount());
            newTraceStatistic.setWeChatCount(newTraceStatistic.getWeChatCount() + traceStatistic.getWeChatCount());
            newTraceStatistic.setArtificialEditCount(newTraceStatistic.getArtificialEditCount() + traceStatistic.getArtificialEditCount());
            newTraceStatistic.setBridgePageCount(newTraceStatistic.getBridgePageCount() + traceStatistic.getBridgePageCount());
            newTraceStatistic.setCompeteProductsCount(newTraceStatistic.getCompeteProductsCount() + traceStatistic.getCompeteProductsCount());
            newTraceStatistic.setOctopusCount(newTraceStatistic.getOctopusCount() + traceStatistic.getOctopusCount());

        }
        return newTraceStatistic;
    }

}
