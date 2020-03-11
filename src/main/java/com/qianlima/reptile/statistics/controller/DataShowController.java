package com.qianlima.reptile.statistics.controller;



import com.qianlima.reptile.statistics.entity.KeyWordDataDetailReq;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.entity.SecondKeyWordReq;
import com.qianlima.reptile.statistics.service.*;
import com.qianlima.reptile.statistics.service.DataShowService;
import com.qianlima.reptile.statistics.service.OctopusMonitorService;
import com.qianlima.reptile.statistics.service.StatisticalService;
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


    @RequestMapping("/datadisplay")
    @ResponseBody
    public Map<String,Object> dataDisplay(@RequestParam Map<String,Object> requestParams){
        return dataShowService.dataDisplay(requestParams);
    }


    /**
     * 故障模板统计
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("/fault_tmplt")
    @ResponseBody
    public Response dataDisplay(String startTime, String endTime){
        List<Map<String, String>> list = statisticalService.statistical(startTime, endTime);
        return Response.success(list);
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
}
