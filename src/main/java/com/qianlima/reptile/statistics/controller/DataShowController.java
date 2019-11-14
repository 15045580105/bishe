package com.qianlima.reptile.statistics.controller;

import com.qianlima.reptile.statistics.service.DataShowService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @RequestMapping("/datashow")
    @ResponseBody
    public Map<String,Object> dataShow(@RequestParam Map<String,Object> requestParams){
        return dataShowService.dataShow(requestParams);
    }
}
