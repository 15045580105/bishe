package com.qianlima.reptile.statistics.controller;



import com.qianlima.reptile.statistics.domain.TmpltAndPotStatistics;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.entity.KeyWordDataDetailReq;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.SecondKeyWordReq;
import com.qianlima.reptile.statistics.mapper.RawdataMapper;
import com.qianlima.reptile.statistics.service.*;
import com.qianlima.reptile.statistics.service.DataShowService;
import com.qianlima.reptile.statistics.service.OctopusMonitorService;
import com.qianlima.reptile.statistics.service.StatisticalService;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-11 13:52
 **/
@RestController
@RequestMapping("/statistics")
public class DataShowController {

    @Resource
    private DataShowService dataShowService;
    @Autowired
    private StatisticalService statisticalService;
    @Autowired
    private FirstKeyWordService FirstKeyWordService;
    @Autowired
    private SecondKeyWordService secondKeyWordService;
    @Autowired
    private KeyWordDataDetailService keyWordDataDetailService;
    @Autowired
    private OctopusMonitorService octopusMonitorService;
    @Autowired
    private CollectPublishTrendService collectPublishTrendService;
    @Autowired
    private TraceStatisticService traceStatisticService;
    @Autowired
    private TemplateAndPotStatistical templateAndPotStatistical;
    @Autowired
    private CollectAndReleaseService collectAndReleaseService;
    @Autowired
    private PublishRateService publishRateService;
    @Autowired
    private NounCalibreService nounCalibreService;
    @Autowired
    private ReleaseHistoricalService releaseHistoricalService;

    @RequestMapping("/datadisplay")
    @ResponseBody
    public Map<String,Object> dataDisplay(@RequestParam Map<String,Object> requestParams){
        return dataShowService.dataDisplay(requestParams);
    }


    /**
     * 故障模板统计
     * @param
     * @param
     * @return
     */
    @PostMapping("/fault_tmplt_data")
    @ResponseBody
    public Response dataDisplay(String startTime, String endTime){
        Map<String,List> map =  statisticalService.statisticalData(startTime, endTime);
        return Response.success(map);
    }

    /**
     * 第一级关键词检索
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("/firstKeyWord")
    public Response firstKeyWordCount(String startTime, String endTime){
        Response response = FirstKeyWordService.firstKeyWordStatistics(startTime, endTime);
        return Response.success(response);
    }

    /**
     * 第二级关键词检索
     * @param secondKeyWordReq
     * @return
     */
    @PostMapping("/secondKeyWord")
    public Response secondKeyWordCount(SecondKeyWordReq secondKeyWordReq){
        Response response = secondKeyWordService.secondKeyWordStatistics(secondKeyWordReq);
        return Response.success(response);
    }

    /**
     * 关键词统计详情
     * @param keyWordDataDetailReq
     * @return
     */
    @PostMapping("/keyWordDataDetail")
    public Response KeyWordDataDetailCount(KeyWordDataDetailReq keyWordDataDetailReq){
        Response response = keyWordDataDetailService.keyWordDataDetail(keyWordDataDetailReq);
        return Response.success(response);
    }


    /**
     * 八爪鱼统计
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping(path = "/octopus")
    public Response getStatistics(String startTime, String endTime) {
        return octopusMonitorService.getOctopusStatistics(startTime, endTime);
    }

    /**
     * 集发布详情-年趋势
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return
     */
    @PostMapping(path = "/yearTrend")
    public Response getYearTrend(String startTime, String endTime) {
        return collectPublishTrendService.getYearTrend(startTime, endTime);
    }

    /**
     * 集发布详情-月趋势
     * @param date 当月日期
     * @return
     */
    @PostMapping(path = "/monthTrend")
    public Response getMonthTrend(String date) {
        return collectPublishTrendService.getMonthTrend(date);
    }

    /**
     * 发布量采集量统计功能
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping(path = "/collect/releas")
    public Response getTraceStatistics(String startTime, String endTime) {
        return traceStatisticService.getTraceStatistic(startTime,endTime);
    }

    /**
     * @return a
     * @description 查询pot模版状态数量接口
     * @author gyx
     * @date 2020-03-23 18:53
    * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/pot_tmplt/count")
    public Response getPotAndPotCount(String startTime, String endTime) {
        Response response = templateAndPotStatistical.selectTemplate(startTime,endTime);
        return response;
    }

    /**
     * @return a
     * @description 查询发布量采集量统计接口
     * @author gyx
     * @date 2020-03-23 18:5
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/collect_releas")
    public Response getCollectReleas(String month) {
        Map<String,String> map = collectAndReleaseService.collectAndReleaseCount(month);
        return Response.success(map);
    }

    /**
     * 每月发布率
     * @param queryMonth
     * @return
     */
    @PostMapping(path = "/publish/rate")
    public Response queryPublishRate(String queryMonth) {
        return publishRateService.queryMonthPublishRate(queryMonth);
    }

    /**
     * 名词口径新增功能
     * @param operator
     * @param content
     * @return
     */
    @PostMapping("/nouncalibre/add")
    public Response addNounCalibre(String operator, String content) {
        return nounCalibreService.addNounCalibre(operator, content);
    }

    /**
     * 名词口径修改功能
     * @param id
     * @param operator
     * @param content
     * @return
     */
    @PutMapping("/nouncalibre/update")
    public Response updateNounCalibre(String id, String operator, String content) {
        return nounCalibreService.updateNounCalibre(id, operator, content);
    }

    /**
     * 名词口径删除功能
     * @param id
     * @return
     */
    @DeleteMapping("/nouncalibre/delete")
    public Response deleteNounCalibre(String id) {
        return nounCalibreService.deleteNounCalibre(id);
    }

    /**
     * 名词口径查询功能
     * @return
     */
    @GetMapping("/nouncalibre/query")
    public Response queryNounCalibre() {
        return nounCalibreService.queryNounCalibre();
    }

    /**
     * @description 历史数据倒入
     * @author gyx
     * @date 2020-03-24 23:54
     * @return
     * @parameter  * @param null
     * @since
     */
    @PostMapping(path = "/historical/data")
    public Response historicalData(String startTime,String endTime) {
        releaseHistoricalService.historical(startTime,endTime);
        return Response.success("");
    }


    /**
     * @description 按月查询每天数据
     * @author gyx
     * @date 2020-03-25 00:35
     * @return
     * @parameter  * @param null
     * @since
     */
    @PostMapping(path = "/template/month")
    public Response templateMonth(String month) {
        return templateAndPotStatistical.selectTemplateMonth(month);
    }

    /**
     * 跑数据
     * @param startTime yyyy-MM-dd
     * @param endTime
     */
    @PostMapping(path = "/collect/releas/rundata")
    public void runData(String startTime , String endTime) {
        traceStatisticService.runData(DateUtils.str2TimeStamp(startTime,DateUtils.FUZSDF),
                DateUtils.str2TimeStamp(endTime,DateUtils.FUZSDF));
    }
}
