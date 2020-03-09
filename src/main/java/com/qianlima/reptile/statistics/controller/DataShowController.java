package com.qianlima.reptile.statistics.controller;



import com.alibaba.fastjson.JSON;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.service.DataShowService;
import com.qianlima.reptile.statistics.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/datadisplay")
    @ResponseBody
    public Map<String,Object> dataDisplay(@RequestParam Map<String,Object> requestParams){
        return dataShowService.dataDisplay(requestParams);
    }


    @RequestMapping("/statistics")
    @ResponseBody
    public Response dataDisplay(String startTime, String endTime){
        List<Map<String, String>> list = statisticalService.statistical(startTime, endTime);
        String s = JSON.toJSONString(list);
        return Response.success(s);
    }

}
