package com.qianlima.reptile.statistics.controller;

import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.service.OctopusMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: OctopusMonitorController
 * @date 2020/3/9 3:38 下午
 */
@RestController
@RequestMapping("/octopus")
public class OctopusMonitorController {
    @Autowired
    private OctopusMonitorService octopusMonitorService;

    @RequestMapping(path = "/query/statistics", method = {RequestMethod.GET, RequestMethod.HEAD})
    public Response getStatistics(String startTime, String endTime) {
        return octopusMonitorService.getOctopusStatistics(startTime, endTime);
    }
}
