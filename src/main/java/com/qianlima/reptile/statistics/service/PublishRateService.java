package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

public interface PublishRateService {

    Response queryPublishRate(String startDate, String endDate);

    Response queryMonthPublishRate(String startDate);
}
