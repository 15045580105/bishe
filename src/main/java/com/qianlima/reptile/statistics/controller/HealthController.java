package com.qianlima.reptile.statistics.controller;

import com.qianlima.reptile.statistics.entity.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tiancl
 * @date 2019-08-27
 */
@RestController
@RequestMapping("/statistics")
public class HealthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    @RequestMapping(path = "/health", method = {RequestMethod.GET, RequestMethod.HEAD})
    public Response health() {
        LOGGER.info("statistics service is  health,time {}", System.currentTimeMillis());
        return new Response.Builder<>().result(1).msg("SUCCESS").build();
    }

}
